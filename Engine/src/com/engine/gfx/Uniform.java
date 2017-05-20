package com.engine.gfx;

import com.engine.misc.DialogWindow;
import org.joml.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.UnknownFormatConversionException;

import static com.engine.core.Engine.gl;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Uniform<T> {
    public enum DataType {
        INT(Integer.class.getName()), FLOAT(Float.class.getName()),
        VEC2(Vector2f.class.getName()), VEC3(Vector3f.class.getName()),
        VEC4(Vector4f.class.getName()), MAT3((Matrix3f.class.getName())),
        MAT4(Matrix4f.class.getName());

        final String className;

        DataType(String className) {
            this.className = className;
        }
    }

    private DataType dataType;
    private String name;
    private T data;
    private FloatBuffer buffer;
    private int location;
    private boolean uploaded;

    public Uniform(String name, T data) {
        this.name = name;
        this.data = data;
        buffer = MemoryUtil.memAllocFloat(16);

        if(data == null) {
            DialogWindow.errorDialog(new GLException(("Uniform \"").concat(name).concat("\" is null")));

            return;
        }

        for(DataType type : DataType.values()) {
            if(data.getClass().getName().equals(type.className)) {
                this.dataType = type;
            }
        }

        if(this.dataType == null) {
            DialogWindow.errorDialog(new GLException(("Uniform \"").concat(name).concat("\": Unknown data type \"").concat(data.getClass().getName()).concat("\"")));
        }
    }

    public Uniform getLocation(ShaderProgram program) {
        location = gl.GetUniformLocation(program.getHandle(), getName());

        return this;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

    public Uniform setData(T data) {
        if(!data.getClass().getName().equals(dataType.className)) {
            try {
                throw new GLException("Data type does not match on uniform \"".concat(getName()).concat("\""));
            } catch (GLException e) {
                DialogWindow.errorDialog(e);
            }
        }

        this.data = data;
        uploaded = false;

        return this;
    }

    public void upload() {
        if(uploaded) {
            return;
        }

        switch(dataType) {
            case INT: {
                gl.Uniform1i(location, (Integer)data);
            } break;
            case FLOAT: {
                gl.Uniform1f(location, (Float)data);
            } break;
            case VEC2: {
                Vector2f vector = (Vector2f)data;
                gl.Uniform2f(location, vector.x, vector.y);
            } break;
            case VEC3: {
                Vector3f vector = (Vector3f)data;
                gl.Uniform3f(location, vector.x, vector.y, vector.z);
            } break;
            case VEC4: {
                Vector4f vector = (Vector4f)data;
                gl.Uniform4f(location, vector.x, vector.y, vector.z, vector.w);
            } break;
            case MAT3: {
                Matrix3f matrix = (Matrix3f)data;
                buffer.clear();
                matrix.get(buffer);
                gl.UniformMatrix3fv(location, false, buffer);
            } break;
            case MAT4: {
                Matrix4f matrix = (Matrix4f)data;
                buffer.clear();
                matrix.get(buffer);
                gl.UniformMatrix4fv(location, false, buffer);
            } break;
        }

        uploaded = true;
    }
}
