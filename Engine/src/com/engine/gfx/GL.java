package com.engine.gfx;

import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by Andrew on 5/28/2016.
 */
public class GL implements IGL {
    public boolean supportsAniso() {
        return org.lwjgl.opengl.GL.getCapabilities().GL_EXT_texture_filter_anisotropic;
    }

    public void Enable(int target) {
        GL11.glEnable(target);
    }

    public void Disable(int target) {
        GL11.glDisable(target);
    }

    public void Viewport(int x, int y, int w, int h) {
        GL11.glViewport(x, y, w, h);
    }

    public void Clear(int mask) {
        GL11.glClear(mask);
    }

    public void ClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    public void BindTexture(int target, int texture) {
        GL11.glBindTexture(target, texture);
    }

    public void TexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
        GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    public void TexParameteri(int target, int pname, int param) {
        GL11.glTexParameteri(target, pname, param);
    }

    public void TexParameterf(int target, int pname, float param) {
        GL11.glTexParameterf(target, pname, param);
    }

    public void TexParameterfv(int target, int pname, float[] params) {
        GL11.glTexParameterfv(target, pname, params);
    }

    public void DeleteTextures(int texture) {
        GL11.glDeleteTextures(texture);
    }

    public void BlendFunc(int sfactor, int dfactor) {
        GL11.glBlendFunc(sfactor, dfactor);
    }

    public void DepthFunc(int func) {
        GL11.glDepthFunc(func);
    }

    public void PolygonMode(int face, int mode) {
        GL11.glPolygonMode(face, mode);
    }

    public void ReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
        GL11.glReadPixels(x, y, width, height, format, type, pixels);
    }

    public void Begin(int mode) {
        GL11.glBegin(mode);
    }

    public void End() {
        GL11.glEnd();
    }

    public void DrawArrays(int mode, int first, int count) {
        GL11.glDrawArrays(mode, first, count);
    }

    public void DrawElements(int mode, int count, int type, long indicesOffset) {
        GL11.glDrawElements(mode, count, type, indicesOffset);
    }

    public void Vertex3f(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
    }

    public void TexCoord2f(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public void FrontFace(int dir) {
        GL11.glFrontFace(dir);
    }

    public void CullFace(int mode) {
        GL11.glCullFace(mode);
    }

    public int GenTextures() {
        return GL11.glGenTextures();
    }

    public int GetInteger(int pname) {
        return GL11.glGetInteger(pname);
    }

    public float GetFloat(int pname) {
        return GL11.glGetFloat(pname);
    }

    public double GetDouble(int pname) {
        return GL11.glGetDouble(pname);
    }

    public String GetString(int pname) {
        return GL11.glGetString(pname);
    }

    public void ActiveTexture(int texture) {
        GL13.glActiveTexture(texture);
    }

    public void BlendEquation(int mode) {
        GL14.glBlendEquation(mode);
    }

    public void BindBuffer(int target, int buffer) {
        GL15.glBindBuffer(target, buffer);
    }

    public void DeleteBuffers(int buffer) {
        GL15.glDeleteBuffers(buffer);
    }

    public void BufferData(int target, ByteBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
    }

    public void BufferSubData(int target, long offset, ByteBuffer data) {
        GL15.glBufferSubData(target, offset, data);
    }

    public int GenBuffers() {
        return GL15.glGenBuffers();
    }

    public void ShaderSource(int shader, CharSequence string) {
        GL20.glShaderSource(shader, string);
    }

    public void CompileShader(int shader) {
        GL20.glCompileShader(shader);
    }

    public void AttachShader(int program, int shader) {
        GL20.glAttachShader(program, shader);
    }

    public void LinkProgram(int program) {
        GL20.glLinkProgram(program);
    }

    public void ValidateProgram(int program) {
        GL20.glValidateProgram(program);
    }

    public void UseProgram(int program) {
        GL20.glUseProgram(program);
    }

    public void DeleteProgram(int program) {
        GL20.glDeleteProgram(program);
    }

    public void DeleteShader(int shader) {
        GL20.glDeleteShader(shader);
    }

    public void DetachShader(int program, int shader) {
        GL20.glDetachShader(program, shader);
    }

    public void Uniform1f(int location, float v0) {
        GL20.glUniform1f(location, v0);
    }

    public void Uniform2f(int location, float v0, float v1) {
        GL20.glUniform2f(location, v0, v1);
    }

    public void Uniform3f(int location, float v0, float v1, float v2) {
        GL20.glUniform3f(location, v0, v1, v2);
    }

    public void Uniform4f(int location, float v0, float v1, float v2, float v3) {
        GL20.glUniform4f(location, v0, v1, v2, v3);
    }

    public void Uniform1i(int location, int v0) {
        GL20.glUniform1i(location, v0);
    }

    public void Uniform2i(int location, int v0, int v1) {
        GL20.glUniform2i(location, v0, v1);
    }

    public void Uniform3i(int location, int v0, int v1, int v2) {
        GL20.glUniform3i(location, v0, v1, v2);
    }

    public void Uniform4i(int location, int v0, int v1, int v2, int v3) {
        GL20.glUniform4i(location, v0, v1, v2, v3);
    }

    public void UniformMatrix2fv(int location, boolean transpose, FloatBuffer value) {
        GL20.glUniformMatrix2fv(location, transpose, value);
    }

    public void UniformMatrix3fv(int location, boolean transpose, FloatBuffer value) {
        GL20.glUniformMatrix3fv(location, transpose, value);
    }

    public void UniformMatrix4fv(int location, boolean transpose, FloatBuffer value) {
        GL20.glUniformMatrix4fv(location, transpose, value);
    }

    public void VertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointerOffset) {
        GL20.glVertexAttribPointer(index, size, type, normalized, stride, pointerOffset);
    }

    public void EnableVertexAttribArray(int index) {
        GL20.glEnableVertexAttribArray(index);
    }

    public void DisableVertexAttribArray(int index) {
        GL20.glDisableVertexAttribArray(index);
    }

    public int CreateProgram() {
        return GL20.glCreateProgram();
    }

    public int CreateShader(int type) {
        return GL20.glCreateShader(type);
    }

    public int GetShaderi(int shader, int pname) {
        return GL20.glGetShaderi(shader, pname);
    }

    public int GetProgrami(int program, int pname) {
        return GL20.glGetProgrami(program, pname);
    }

    public int GetUniformLocation(int program, CharSequence name) {
        return GL20.glGetUniformLocation(program, name);
    }

    public String GetShaderInfoLog(int shader) {
        return GL20.glGetShaderInfoLog(shader);
    }

    public String GetProgramInfoLog(int program) {
        return GL20.glGetProgramInfoLog(program);
    }

    public void GenerateMipmap(int target) {
        GL30.glGenerateMipmap(target);
    }

    public void BindFramebuffer(int target, int framebuffer) {
        GL30.glBindFramebuffer(target, framebuffer);
    }

    public void DeleteFramebuffers(int framebuffer) {
        GL30.glDeleteFramebuffers(framebuffer);
    }

    public void BindRenderbuffer(int target, int renderbuffer) {
        GL30.glBindRenderbuffer(target, renderbuffer);
    }

    public void DeleteRenderbuffers(int renderbuffer) {
        GL30.glDeleteRenderbuffers(renderbuffer);
    }

    public void FramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
        GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
    }

    public void FramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
        GL30.glFramebufferTextureLayer(target, attachment, texture, level, layer);
    }

    public void FramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
        GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
    }

    public void BlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        GL30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }

    public void RenderbufferStorage(int target, int internalformat, int width, int height) {
        GL30.glRenderbufferStorage(target, internalformat, width, height);
    }

    public void RenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
        GL30.glRenderbufferStorageMultisample(target, samples, internalformat, width, height);
    }

    public void BindVertexArray(int array) {
        GL30.glBindVertexArray(array);
    }

    public void DeleteVertexArrays(int array) {
        GL30.glDeleteVertexArrays(array);
    }

    public int GenFramebuffers() {
        return GL30.glGenFramebuffers();
    }

    public int CheckFramebufferStatus(int target) {
        return GL30.glCheckFramebufferStatus(target);
    }

    public int GenRenderbuffers() {
        return GL30.glGenRenderbuffers();
    }

    public int GenVertexArrays() {
        return GL30.glGenVertexArrays();
    }

    public void DrawArraysInstanced(int mode, int first, int count, int primcount) {
        GL31.glDrawArraysInstanced(mode, first, count, primcount);
    }

    public void DrawElementsInstanced(int mode, int count, int type, long indices, int primcount) {
        GL31.glDrawElementsInstanced(mode, count, type, indices, primcount);
    }

    public void FramebufferTexture(int target, int attachment, int texture, int level) {
        GL32.glFramebufferTexture(target, attachment, texture, level);
    }
}
