package com.example.user.loco;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;



public class TCPClient {

    public static String serverMessage;
    public static final String SERVERIP = "locoapp.ddns.net"; //your computer IP address
    public static final int SERVERPORT = 1234;
    public static OnMessageReceived mMessageListener = null;
    public static boolean mRun = false;
    //public static TinyDB tcpTdb;
    public static Socket socket;

    public static PrintWriter out;
    public static BufferedReader in;


    public TCPClient() {

    }

    //listener listen for messages from server
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    //sending client message
    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            final String mes = message;
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        out.println(mes);
                        out.flush();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();//test

        }
    }

    public void sendFile(String image) {//need to add my name and to who i am sending the picture
        final File imag = new File(image);

        try {
            File sized = saveBitmapToFile(imag);

            try {
                byte[] myarray = FileUtils.readFileToByteArray(sized);

                if (myarray!=null) {
                    String im = "1000@";

                    new User(MainActivity.cpn);
                    String userN = User.username;

                    im += userN;
                    im += ",";

                    String pic = Base64.encodeToString(myarray, Base64.DEFAULT);
                    im += pic;

                    im += "*";
                    sendMessage(im);
                }
            }catch (Exception a){Toast.makeText(NavigationDrawerFragment.NavigationCpn, "somthing wen wrong...", Toast.LENGTH_SHORT).show();}


        } catch (NullPointerException A) { Toast.makeText(NavigationDrawerFragment.NavigationCpn, "ops try again please", Toast.LENGTH_SHORT).show();}







    }

    public void stopClient() {
        mRun = false;
    }


    public static void run() {

        mRun = true;
        try {
            //connecting
           // if (MainActivity.cpn!=null){tcpTdb = new TinyDB(MainActivity.cpn);}
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            socket = new Socket(serverAddr, SERVERPORT);



            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
                //receive the message that the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                HelloService.firstTouch();
               // if (!MainActivity.ServerOnline.equals("1")){MainActivity.ActionBarServer("1");}
                //MainActivity.ActionBarServer("1");
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage="";
                   do{
                        serverMessage += in.readLine();
                        char lastC = serverMessage.charAt(serverMessage.length()-1);
                       if(lastC!= '*') serverMessage +="\n";
                    } while(serverMessage.charAt(serverMessage.length()-1)!= '*'&&!serverMessage.equals("null\n"));
                    if (!serverMessage.equals("null\n") && mMessageListener != null&&!serverMessage.equals("")) {
                        //call the method messageReceived from MyActivity class to inform main activity that a message has received
                        mMessageListener.messageReceived(serverMessage);
                    }
                    else
                    {
                        //if (!MainActivity.ServerOnline.equals("0")){MainActivity.ActionBarServer("0");}
                        MainActivity.ActionBarServer("0");
                        run();
                    }
                   // serverMessage = null;
                }

            } catch (Exception e) {

            } finally {


                socket.close();
                run();
            }

        } catch (Exception e) {

            if (MainActivity.active)
            {
               // if (!MainActivity.ServerOnline.equals("0")){MainActivity.ActionBarServer("0");}
                MainActivity.ActionBarServer("0");
                SystemClock.sleep(10000);//need to check when server works
                MainActivity.cpn.startService(new Intent(MainActivity.cpn, HelloService.class));//may causing problems

            }
            else{
                SystemClock.sleep(10000);//need to check when server works
                run();

            }
        }
        //run();


    }

    //Declare the interface. The method messageReceived(String message) will be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
         void messageReceived(String message);
    }

    public static File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/LoCotemp";
            File dir = new File(file_path);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            File flnovoer = new File(dir,"temp.jpg");
            FileOutputStream outputStream = new FileOutputStream(flnovoer);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 40 , outputStream);

            return flnovoer;
        } catch (Exception e) {
            return null;
        }
    }

}