package com.boostcms.common.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Created by DongYibo on 2019/3/7.
 */

public class EnumToIntHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private Class<E> type;

    public EnumToIntHandler(Class<E> type){
        if(type == null){
            throw new IllegalArgumentException("Type argument cannot be null");
        }else {
            this.type = type;
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        if(jdbcType == null){
            Method method = null;
            Integer value = null;
            try {
                Class<?> clazz = e.getClass();
                method = clazz.getMethod("getValue",null);
                value = (Integer) method.invoke(e);
            } catch (NoSuchMethodException e1) {
                preparedStatement.setString(i,e.toString());
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            preparedStatement.setInt(i, value);
        }else{
            preparedStatement.setObject(i,e.name(),jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return get(resultSet.getString(s));
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return get(resultSet.getString(i));
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return get(callableStatement.getString(i));
    }

    private <E extends Enum<E>> E get(String v){
        if(v == null){
            return null;
        }
        if(StringUtils.isNumeric(v)){
            return get((Class<E>)type,Integer.parseInt(v));
        }else{
            return Enum.valueOf((Class<E>)type,v);
        }
    }

    private <E extends Enum<E>> E get(Class<E> type,int v){
        Method method = null;
        E value = null;
        try {
            method = type.getMethod("getInstance", int.class);
            E[] es = type.getEnumConstants();
            value = (E) method.invoke(es[0],v);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }


}
