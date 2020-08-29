package com.leolee.msf.test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class Tests extends Thread {

    private Pipe pipe;

    public Tests(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void run() {
        try {
            this.pipeWrite(pipe);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //--------------------------工具---------------------------------

    /**
     * String 转换 ByteBuffer
     *
     * @param str
     * @return
     */
    public static ByteBuffer getByteBuffer(String str) {
        return ByteBuffer.wrap(str.getBytes());
    }

    /**
     * ByteBuffer 转换 String
     *
     * @param buffer
     * @return
     */
    public static String getString(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    //--------------------------IO和NIO的比较-------------------------------

    /**
     * 常规方法读取文件
     *
     * @param filePath
     */
    public static void nomalReadFileContent(String filePath) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while (bytesRead != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    System.out.print((char) buf[i]);
                }
                bytesRead = in.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * NIO读取文件
     *
     * @param filePath
     */
    public static void nioReadFileContent(String filePath) {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);
            while (bytesRead != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.compact();
                bytesRead = fileChannel.read(buf);
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取文件字节长度
     *
     * @param filePath
     * @throws IOException
     */
    public static void getFileByteLenth(String filePath) throws IOException {
        File targetFile = new File(filePath);
        FileInputStream fis = new FileInputStream(targetFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        byte[] fb = bos.toByteArray();
        System.out.println(fb.length);
    }


    /**
     * 获取文件字节长度
     *
     * @param filePath
     * @throws IOException
     */
    public static void getFileChannelSize(String filePath) throws IOException {
        File targetFile = new File(filePath);
        FileInputStream fis = new FileInputStream(targetFile);
        FileChannel fc = fis.getChannel();
        System.out.println(fc.size());
    }


    /**
     * NIO:Selector测试
     *
     * @param filePath
     * @throws IOException
     */
    public static void nioSelector(String filePath) throws IOException {

        //注册通道
        SocketChannel sc = SocketChannel.open();
        Selector selector = Selector.open();
        sc.configureBlocking(false);//非阻塞模式
        SelectionKey selectionKey = sc.register(selector, SelectionKey.OP_READ);

        //遍历selector中的通道
        Set selectedKeys = selector.selectedKeys();
        Iterator keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = (SelectionKey) keyIterator.next();
            if (key.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel
            } else if (key.isConnectable()) {
                // a connection was established with a remote server.
            } else if (key.isReadable()) {
                // a channel is ready for reading
            } else if (key.isWritable()) {
                // a channel is ready for writing
            }
            //注意每次迭代末尾的keyIterator.remove()调用。Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中
            keyIterator.remove();
        }
    }

    /**
     * NIO:DatagramChannel测试
     *
     * @throws IOException
     */
    public static void nioDatagramChannelTest() throws IOException {
        //打开的 DatagramChannel可以在UDP端口9999上接收数据包
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));

        //通过receive()方法从DatagramChannel接收数据
        //receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        channel.receive(buf);

        //发送一串字符到”jenkov.com”服务器的UDP端口80。 因为服务端并没有监控这个端口，所以什么也不会发生。也不会通知你发出的数据包是否已收到,因为UDP在数据传送方面没有保证
        String newData = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer bf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();
        int bytesSent = channel.send(bf, new InetSocketAddress("jenkov.com", 80));

        //连接到特定地址
        channel.connect(new InetSocketAddress("jenkov.com", 80));
        //当连接后，也可以使用read()和write()方法，就像在用传统的通道一样。只是在数据传送方面没有任何保证
        int bytesRead = channel.read(buf);
        int bytesWritten = channel.write(buf);
    }


    /**
     * Pipe写数据
     *
     * @param pipe
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized void pipeWrite(Pipe pipe) throws IOException, InterruptedException {
        Pipe.SinkChannel sinkChannel = pipe.sink();
        String newData = "New String to write to file..." + System.currentTimeMillis();
        Thread.sleep(5000);
        String lineSeparator = System.getProperty("line.separator");
        System.out.println(lineSeparator);
        System.out.println("正在写入数据");
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();
        while (buf.hasRemaining()) {
            sinkChannel.write(buf);
        }

    }

    /**
     * Pipe读数据
     *
     * @param pipe
     * @throws IOException
     */
    public void pipeRead(Pipe pipe) throws IOException {

        Pipe.SourceChannel sourceChannel = pipe.source();
        ByteBuffer buf = ByteBuffer.allocate(48);
//        int bytesRead = sourceChannel.read(buf);
//        System.out.println(bytesRead);
        while (sourceChannel.read(buf) > 0) {
            //limit is set to current position and position is set to zero
            buf.flip();
            while (buf.hasRemaining()) {
                char ch = (char) buf.get();
                System.out.print(ch);
            }
            //position is set to zero and limit is set to capacity to clear the buffer.
            buf.clear();
        }
    }


    public void nioFileChannelTest() {

    }


    public static void main(String[] args) throws IOException, InterruptedException {
////        Tests.nomalReadFileContent("C:" + File.separator + "Users" + File.separator + "LeoLee" + File.separator + "Desktop" + File.separator + "test.txt");
        Tests.nioReadFileContent("C:" + File.separator + "Users" + File.separator + "LeoLee" + File.separator + "Desktop" + File.separator + "test.txt");
//
////        Tests.getFileByteLenth("C:" + File.separator + "Users" + File.separator + "LeoLee" + File.separator + "Desktop" + File.separator + "test.txt");
////        Tests.getFileChannelSize("C:" + File.separator + "Users" + File.separator + "LeoLee" + File.separator + "Desktop" + File.separator + "test.txt");
//
//        //创建定长线程池
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        //创建管道
//        Pipe pipe = Pipe.open();
//        //开启管道读数据
//        Tests2 tests2 = new Tests2(pipe);
//        pool.submit(tests2);
//        //开启管道写数据
//        Tests tests = new Tests(pipe);
//        Thread.sleep(500);
//        for (int i = 0; i < 3; i++) {
//            System.out.println("第"+ (i+1) +"写入数据进程开始执行");
//            pool.submit(tests);
//        }
//        System.out.println(Thread.currentThread().getName());
    }
}
