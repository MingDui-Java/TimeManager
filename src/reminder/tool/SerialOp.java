package reminder.tool;

import reminder.entity.ReviewTask;

import java.io.*;
import java.util.List;

/**
 * @author 86155
 **/
public class SerialOp<T> {
    File serFile = new File("./data/");
    public void ser(File f, T tt) throws IOException {
        //判断目录是否存在
        if (!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        //判断文件是否存在
        if (!f.exists()){
            f.createNewFile();
        }
        //创建一个字节输出流对象
        OutputStream ops=new FileOutputStream(f);
        //创建一个实现序列化的对象
        ObjectOutputStream oos=new ObjectOutputStream(ops);
        //实现序列化
        oos.writeObject(tt);
        oos.close();
        ops.close();
    }

    public T dSer(File f) throws Exception {
        T tt = null;
        //创建一个字节输入流
        InputStream in = new FileInputStream(f);
        //创建一个实现反序列化的对象
        ObjectInputStream input = new ObjectInputStream(in);
        //实现反序列化
        try {
            tt = (T) input.readObject();
        }catch (EOFException ignored) {
            System.out.println("error!");
        }
        //关闭
        input.close();
        in.close();
        return tt;
    }
}
