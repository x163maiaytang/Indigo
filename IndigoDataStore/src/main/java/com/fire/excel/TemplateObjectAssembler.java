package com.fire.excel;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class TemplateObjectAssembler
{
	private static final String ARRAY_SPLIT = ",";
	private static final String OBJATTRIBUTE_SPLIT = ",";
	private static final String COLLECTION_SPLIT = ";";
	public static final Map classFields = new HashMap();
	public static final Map classMethods = new HashMap();

	public TemplateObjectAssembler()
	{
	}

	public void doAssemble(Object obj, Row row, Class clazz)
			throws Exception
	{
		Class superClazz = clazz.getSuperclass();
		if (superClazz != null
				&& superClazz.isAnnotationPresent(ExcelRowBinding.class))
			doAssemble(obj, row, superClazz);
		Field fields[] = null;
		if (classFields.containsKey(clazz))
		{
			fields = (Field[]) classFields.get(clazz);
		} else
		{
			fields = clazz.getDeclaredFields();
			Field.setAccessible(fields, true);
			classFields.put(clazz, fields);
		}

		
		int fieldsLengthOffset = 0; 
		while(superClazz != null)
		{
			if(superClazz.isAnnotationPresent(ExcelRowBinding.class))
			{
				for(Field f:superClazz.getDeclaredFields())
				{
					if(f.isAnnotationPresent(ExcelCellBinding.class))
						fieldsLengthOffset ++;	
				}
			}
			superClazz = superClazz.getSuperclass();
		}
		
		for (int i = 0; i < fields.length; i++)
		{
			if ((fields[i].getModifiers() & 8) != 0)
			{
				-- fieldsLengthOffset ; 
				continue;
			}
			if (fields[i].isAnnotationPresent(ExcelCellBinding.class))
			{
				try
				{
//					ExcelCellBinding binding = (ExcelCellBinding) fields[i]
//							.getAnnotation(ExcelCellBinding.class);
					Class fieldType = fields[i].getType();
					Object fValue = getFieldValue(fields[i], row, fieldType,
							i + fieldsLengthOffset);
					invokeSetMethod(clazz, fields[i], obj, fValue);
				} catch (Exception e)
				{
					throw e;
				} catch (Throwable e)
				{
					e.printStackTrace();
				}
				continue;
			}
			if (fields[i].isAnnotationPresent(ExcelRowBinding.class))
			{
//				if (clazz.getName().indexOf("HumanQuestTemplateVO") > 0)
//					System.out.println(clazz.getName());
				Class fieldType = fields[i].getType();
				Object subObject = fieldType.newInstance();
				invokeSetMethod(clazz, fields[i], obj, subObject);
				doAssemble(subObject, row, fieldType);
				continue;
			}
			if (fields[i].isAnnotationPresent(ExcelCollectionMapping.class))
			{
				insertCollection(fields[i], obj, row);
				continue;
			}
			if (fields[i].isAnnotationPresent(ExcelRowMapping.class))
			{
				insertRow(fields[i], obj, row);
				continue;
			}
			-- fieldsLengthOffset; 
		}

	}

	public static boolean invokeSetMethod(Class clazz, Field field, Object obj,
			Object value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException
	{
		String name = field.getName();
		StringBuilder mNameBuilder = new StringBuilder();
		mNameBuilder.append("set");
		Class fType = field.getType();
		if ((fType == Boolean.TYPE || fType == Boolean.class)
				&& name.startsWith("is"))
		{
			mNameBuilder.append(name.substring(2, 3).toUpperCase());
			mNameBuilder.append(name.substring(3));
		} else
		{
			mNameBuilder.append(name.substring(0, 1).toUpperCase());
			mNameBuilder.append(name.substring(1));
		}
		String methodName = mNameBuilder.toString();
		Method methods[] = null;
		if (classMethods.containsKey(clazz))
		{
			methods = (Method[]) classMethods.get(clazz);
		} else
		{
			methods = clazz.getDeclaredMethods();
			Method.setAccessible(methods, true);
			classMethods.put(clazz, methods);
		}
		Method setMethod = searchSetterMethod(methods, methodName,
				field.getType());
		try
		{
			setMethod.invoke(obj, new Object[] { value });
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	private static Method searchSetterMethod(Method methods[], String name,
			Class parameterType)
	{
		Method m = null;
		String internedName = name.intern();
		for (int i = 0; i < methods.length; i++)
		{
			m = methods[i];
			if (m.getName() == internedName
					&& m.getParameterTypes().length == 1
					&& parameterType == m.getParameterTypes()[0])
				return m;
		}

		return null;
	}

	private void insertCollection(Field field, Object obj, Row row)
	{
		ExcelCollectionMapping ecm = (ExcelCollectionMapping) field
				.getAnnotation(ExcelCollectionMapping.class);
		Class fieldType = field.getType();
		try
		{
			Class arr$[] = field.getType().getInterfaces();
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				Class tmp = arr$[i$];
				if (tmp == List.class || tmp == Set.class || tmp == Map.class)
					fieldType = tmp;
			}

			if (fieldType == List.class)
			{
				List list = (List) field.get(obj);
				List fieldValue = list;
				if (fieldValue == null)
					fieldValue = new ArrayList();
				insertSetOrList(fieldValue, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj,
						fieldValue);
			} else if (fieldType == Set.class)
			{
				Set fieldValue = (Set) field.get(obj);
				if (fieldValue == null)
					fieldValue = new HashSet();
				insertSetOrList(fieldValue, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj,
						fieldValue);
			} else if (fieldType == Map.class)
			{
				Map fieldValue = (Map) field.get(obj);
				if (fieldValue == null)
					fieldValue = new HashMap();
				insertMap(fieldValue, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj,
						fieldValue);
			} else if (fieldType.isArray())
			{
				Object arr = getArray(fieldType, ecm, row);
				invokeSetMethod(field.getDeclaringClass(), field, obj, arr);
			} else
			{
				throw new ConfigException((new StringBuilder())
						.append("Unsupported field type :").append(fieldType)
						.toString());
			}
		} catch (Exception e)
		{
			throw new ConfigException(e);
		}
	}

	private void insertRow(Field field, Object obj, Row row)
			throws Exception
	{
		ExcelRowMapping anno = (ExcelRowMapping) field
				.getAnnotation(ExcelRowMapping.class);
		Object rowObject = getRowObject(field.getType(), anno, row);
		invokeSetMethod(field.getDeclaringClass(), field, obj, rowObject);
	}

	private Object getRowObject(Class fieldType, ExcelRowMapping ecm,
			Row row) throws Exception
	{
		String cn = ecm.rowNumber();
		int offsets[] = StringUtils.getIntArray(cn, ",");
		Object result = fieldType.newInstance();
		Field arr$[] = fieldType.getDeclaredFields();
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Field field = arr$[i$];
			BeanFieldNumber anno = (BeanFieldNumber) field
					.getAnnotation(BeanFieldNumber.class);
			if (anno != null)
			{
				int offset = offsets[anno.number() - 1];
				Object value = getFieldValue(field, row, field.getType(),
						offset);
				invokeSetMethod(fieldType, field, result, value);
			}
		}

		return result;
	}

	private Object getArray(Class fieldType, ExcelCollectionMapping ecm,
			Row row) throws Exception
	{
		Class element_cl = fieldType.getComponentType();
		String cn = ecm.collectionNumber();
		String cns[] = cn.split(";");
		Object arr = Array.newInstance(element_cl, cns.length);
		for (int i = 0; i < cns.length; i++)
		{
			String strs[] = cns[i].split(",");
			Object o = null;
			if (element_cl.isAnnotationPresent(ExcelRowBinding.class))
			{
				o = getElementObject(element_cl, strs, row);
			} else
			{
				if (strs.length > 1)
					throw new ConfigException("cell's number greater than 1");
				o = getFieldValue(null, row, element_cl,
						(new Integer(strs[0])).intValue());
			}
			Array.set(arr, i, o);
		}

		return arr;
	}

	private void insertSetOrList(Collection col, ExcelCollectionMapping ecm,
			Row row) throws Exception
	{
		Class element_cl = ecm.clazz();
		String cn = ecm.collectionNumber();
		String cns[] = cn.split(";");
		String arr$[] = cns;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			String str = arr$[i$];
			String strs[] = str.split(",");
			Object o = null;
			if (element_cl.isAnnotationPresent(ExcelRowBinding.class))
			{
				o = getElementObject(element_cl, strs, row);
			} else
			{
				if (strs.length > 1)
					throw new ConfigException(String.format(
							"cell's number greater than 1 on row(%d)",
							new Object[] { Integer.valueOf(row.getRowNum()) }));
				o = getFieldValue(null, row, element_cl,
						(new Integer(strs[0])).intValue());
			}
			col.add(o);
		}

	}

	private void insertMap(Map map, ExcelCollectionMapping ecm, Row row)
			throws Exception
	{
		Class element_cl = ecm.clazz();
		String cn = ecm.collectionNumber();
		String cns[] = cn.split(";");
		String arr$[] = cns;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			String str = arr$[i$];
			String strs[] = str.split(",");
			String keyCell = strs[0];
			Cell cell = row.getCell(Integer.parseInt(keyCell));
			String key = PoiUtils.getStringValue(cell);
			String strs_other[] = new String[strs.length - 1];
			for (int i = 1; i < strs.length; i++)
				strs_other[i - 1] = strs[i];

			Object o = null;
			if (element_cl.isAnnotationPresent(ExcelRowBinding.class))
				o = getElementObject(element_cl, strs_other, row);
			else
				o = getFieldValue(null, row, element_cl, (new Integer(
						strs_other[0])).intValue());
			map.put(key, o);
		}

	}

	private Object getElementObject(Class clazz, String strs[], Row row)
	{
		Object member_obj = null;
		try
		{
			member_obj = clazz.newInstance();
			Field fields[] = null;
			if (classFields.containsKey(clazz))
			{
				fields = (Field[]) classFields.get(clazz);
			} else
			{
				fields = clazz.getDeclaredFields();
				Field.setAccessible(fields, true);
				classFields.put(clazz, fields);
			}
			Map refFields = new HashMap();
			Field arr$[] = fields;
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				Field field = arr$[i$];
				if (field.isAnnotationPresent(BeanFieldNumber.class))
					refFields.put(Integer.valueOf(((BeanFieldNumber) field
							.getAnnotation(BeanFieldNumber.class)).number()),
							field);
			}

			for (int j = 0; j < strs.length; j++)
			{
				int number = Integer.parseInt(strs[j]);
				Field curField = (Field) refFields.get(Integer.valueOf(j + 1));
				if (curField != null)
				{
					Object fValue = getFieldValue(curField, row,
							curField.getType(), number);
					invokeSetMethod(curField.getDeclaringClass(), curField,
							member_obj, fValue);
				}
			}

		} catch (Exception e)
		{
			throw new ConfigException(e);
		}
		return member_obj;
	}

	private Object getFieldValue(Field field, Row row, Class fieldType,
			int offset) throws Exception
	{
		Cell cell = row.getCell(offset);
		if (fieldType == Integer.TYPE || fieldType == Integer.class)
			return Integer.valueOf(PoiUtils.getIntValue(cell));
		if (fieldType == Long.TYPE || fieldType == Long.class)
			return Long.valueOf((long) PoiUtils.getDoubleValue(cell));
		if (fieldType == Short.TYPE || fieldType == Short.class)
			return Short.valueOf(PoiUtils.getShortValue(cell));
		if (fieldType == Byte.TYPE || fieldType == Byte.class)
			return Byte.valueOf(PoiUtils.getByteValue(cell));
		if (fieldType == Double.TYPE || fieldType == Double.class)
			return Double.valueOf(PoiUtils.getDoubleValue(cell));
		if (fieldType == Float.TYPE || fieldType == Float.class)
			return Float.valueOf(PoiUtils.getFloatValue(cell));
		if (fieldType == Date.class)
			return PoiUtils.getDateValue(cell, null);
		if (fieldType == Calendar.class)
			return PoiUtils.getCalendarValue(cell);
		if (fieldType == String.class)
		{
			String str = PoiUtils.getStringValue(cell);
			return str;
		}
		if (fieldType == Boolean.class || fieldType == Boolean.TYPE)
		{
			int v = PoiUtils.getIntValue(cell);
			if (v == 0)
				return Boolean.FALSE;
			if (v == 1)
				return Boolean.TRUE;
			else
				throw new ConfigException((new StringBuilder())
						.append("boolean type value error :").append(v)
						.toString());
		}
		if (fieldType.isEnum() && IndexedEnum.class.isAssignableFrom(fieldType))
		{
			int v = PoiUtils.getIntValue(cell);
			IndexedEnum values[] = (IndexedEnum[]) (IndexedEnum[]) fieldType
					.getEnumConstants();
			for (int i = 0; i < values.length; i++)
				if (values[i].getIndex() == v)
					return values[i];

			throw new ConfigException((new StringBuilder())
					.append("Illegal Enum value:").append(v).toString());
		}
		if (fieldType.isArray())
		{
			Class componentType = fieldType.getComponentType();
			String v = PoiUtils.getStringValue(cell);
			String vs[] = v.split(",");
			if (componentType == String.class)
				return vs;
			if (vs.length == 1 && vs[0].trim().equals(""))
			{
				return Array.newInstance(componentType, 0);
			} else
			{
				Object result = Array.newInstance(componentType, vs.length);
				convertValueToType(vs, result, componentType);
				return result;
			}
		} else
		{
			throw new ConfigException((new StringBuilder())
					.append("Unsupported field type :").append(fieldType)
					.toString());
		}
	}

	private void convertValueToType(String values[], Object result,
			Class fieldType)
	{
		if (fieldType == Integer.TYPE || fieldType == Integer.class)
		{
			for (int i = 0; i < values.length; i++)
				Array.setInt(result, i, (int) Double.parseDouble(values[i]));

		} else if (fieldType == Long.TYPE || fieldType == Long.class)
		{
			for (int i = 0; i < values.length; i++)
				Array.setLong(result, i, (long) Double.parseDouble(values[i]));

		} else if (fieldType == Short.TYPE || fieldType == Short.class)
		{
			for (int i = 0; i < values.length; i++)
				Array.setShort(result, i,
						(short) (int) Double.parseDouble(values[i]));

		} else if (fieldType == Double.TYPE || fieldType == Double.class)
		{
			for (int i = 0; i < values.length; i++)
				Array.setDouble(result, i, Double.parseDouble(values[i]));

		} else if (fieldType == Float.TYPE || fieldType == Float.class)
		{
			for (int i = 0; i < values.length; i++)
				Array.setFloat(result, i, (float) Double.parseDouble(values[i]));

		} else if (fieldType == Boolean.class || fieldType == Boolean.TYPE)
		{
			for (int i = 0; i < values.length; i++)
			{
				int v = (int) Double.parseDouble(values[i]);
				if (v == 0)
					Array.setBoolean(result, i, Boolean.FALSE.booleanValue());
				else if (v == 1)
					Array.setBoolean(result, i, Boolean.TRUE.booleanValue());
				else
					throw new ConfigException((new StringBuilder())
							.append("boolean type value error :").append(v)
							.toString());
			}

		} else if (fieldType.isEnum()
				&& IndexedEnum.class.isAssignableFrom(fieldType))
		{
			for (int i = 0; i < values.length; i++)
			{
				int v = (int) Double.parseDouble(values[i]);
				boolean isSet = false;
				IndexedEnum enums[] = (IndexedEnum[]) (IndexedEnum[]) fieldType
						.getEnumConstants();
				int j = 0;
				do
				{
					if (j >= enums.length)
						break;
					if (enums[j].getIndex() == v)
					{
						Array.set(result, i, enums[j]);
						isSet = true;
						break;
					}
					j++;
				} while (true);
				if (!isSet)
					throw new ConfigException((new StringBuilder())
							.append("Illegal Enum value:").append(v).toString());
			}

		} else
		{
			throw new ConfigException((new StringBuilder())
					.append("Unsupported Array component type  :")
					.append(fieldType).toString());
		}
	}

}
