package com.leolee.msf.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class Tests2 extends Thread {

    private Pipe pipe;

    public Tests2(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void run() {
        try {
            this.pipeRead(pipe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pipe读数据
     *
     * @param pipe
     * @throws IOException
     */
    public void pipeRead(Pipe pipe) throws IOException {
        System.out.println("开始接收接受数据");
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
        System.out.println("接受数据成功");
    }
}
