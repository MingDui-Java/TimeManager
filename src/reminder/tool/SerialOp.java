/**
 * 提醒事项和喝水提醒的工具包
 *
 * @author DdddM
 * @version 1.0
 */
package reminder.tool;

import reminder.entity.ReviewTask;

import java.io.*;
import java.util.List;

/**
 * 序列化相关操作<BR/>
 * 采用泛型，适用于对应类T
 *
 * @author DdddM
 * @version 1.0
 * @param <T> 对泛型T进行序列化相关操作
 **/
public class SerialOp<T> {
    /**
     * 序列化相关操作的构造函数
     */
    public SerialOp() {

    }
    /**
     * 序列化操作
     *
     * @param f 序列化的目的文件
     * @param obj 序列化的对象
     * @throws IOException 序列化中出现的异常
     */
    public void ser(File f, T obj) throws IOException {
        try {//判断目录是否存在
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            //判断文件是否存在
            if (!f.exists()) {
                f.createNewFile();
            }
            //创建一个字节输出流对象
            OutputStream ops = new FileOutputStream(f);
            //创建一个实现序列化的对象
            ObjectOutputStream oos = new ObjectOutputStream(ops);
            //实现序列化
            oos.writeObject(obj);
            oos.close();
            ops.close();
        }catch(IOException e) {
            throw new IOException(e);
        }
    }
    /**
     * 反序列化操作
     *
     * @param f 反序列化的来源文件
     * @return 序列化对象
     * @throws Exception 反序列化中出现的异常
     */
    public T dSer(File f) throws Exception {
        try {
            T tt = null;
            //创建一个字节输入流
            InputStream in = new FileInputStream(f);
            //创建一个实现反序列化的对象
            ObjectInputStream input = new ObjectInputStream(in);
            //实现反序列化
            try {
                tt = (T) input.readObject();
            } catch (EOFException ignored) {
                System.out.println("error!");
            }
            //关闭
            input.close();
            in.close();
            return tt;
        }catch (Exception e) {
            throw new Exception(e);
        }
    }
}
