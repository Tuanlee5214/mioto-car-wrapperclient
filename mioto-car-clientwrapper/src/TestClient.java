
import error.Err;
import org.apache.log4j.Logger;
import thrift.TLoginInfo;
import thrift.TLoginResult;
import thrift.TSignUpRequest;
import wrapper.CarClientWrapper;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tuanlee
 */
public class TestClient {
    private static final Logger _Logger = Logger.getLogger(TestClient.class);
    private TestClient(){}
    private static final Object lock = new Object();
    private static final String host = "127.0.0.1";
    private static final int port = 10101;
    private static final int timeout = 3000;
    private static final String source = "mioto-car-web";
    private static volatile CarClientWrapper cli = new CarClientWrapper(host, port, timeout, source);
    
    public static void main(String[] args)
    {
        String env  = System.getProperty("appenv", "development");
        String conf = System.getProperty("conf", "conf");
        org.apache.log4j.PropertyConfigurator.configure(
                conf + java.io.File.separator + env + ".log4j.ini");
        TSignUpRequest sr = new TSignUpRequest();
        sr.setPhone("0977821240");
        sr.setPwd("hahahahahaha");
        sr.setDisplayName("Tuanlee");
        sr.setEmail("ronaldo123@gmail.com");
        TLoginInfo info = new TLoginInfo();
        info.setUserAgent("ronaldo");
        info.setUserIP("127.0.0.1");
        TLoginResult ret = cli.signup(sr, info);
        if(Err.isSuccess(ret.getError()))
        {
            System.out.println("Sign up successfully with userId = " + String.valueOf(ret.getUser().getUserId()));
        }
        else System.out.println("Sign up failed" + String.valueOf(ret.getError()) + " " + ret.getMessage());
        
    }
}
