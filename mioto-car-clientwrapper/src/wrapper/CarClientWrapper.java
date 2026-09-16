/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wrapper;

import error.Err;
import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.MiotoCarService;
import thrift.OpHandle;
import thrift.TLoginInfo;
import thrift.TLoginRequest;
import thrift.TLoginResult;
import thrift.TLogoutResult;
import thrift.TSessionResult;
import thrift.TSignUpRequest;
import thrift.TUpdateUserResult;
import thrift.TUser;
import thrift.TUserResult;

/**
 *
 * @author tuanlee
 */
public class CarClientWrapper {
    private final String _host;
    private final int _port;
    private final int _timeout;
    private final OpHandle _handle;
    private final String _source;
    private static final Logger _Logger = Logger.getLogger(CarClientWrapper.class);
    public CarClientWrapper(String host, int port, int timeout, String source)
    {
        _handle = new OpHandle();
        _handle.setSource(source);
        _handle.setAppName(System.getProperty("appname", "car-client"));
        _handle.setIp("127.0.0.1");
        _host = host;
        _port = port;
        _timeout = timeout;
        _source = source;
    }
    
    
    private interface Call<R> {
        R exec(MiotoCarService.Client client) throws Exception;
    }
    
    private <R> R execute(Call<R> call, R errorValue)
    {
        TTransport transport = null;
        try {
            transport = new TSocket(_host, _port, _timeout);   
            transport.open();
            _Logger.info("Before calling signup from wrapper client");
            return call.exec(new MiotoCarService.Client(new TBinaryProtocol(transport)));
        } catch (Exception ex) {
            _Logger.error("Failed to call thrift");
            return errorValue;
        } finally {
            if (transport != null) {
                transport.close();                              
            }
        }

    }
    
    public TLoginResult signup(final TSignUpRequest req, TLoginInfo info)
    {
        return execute(new Call<TLoginResult>() {
            @Override
            public TLoginResult exec(MiotoCarService.Client client) throws Exception {
                _Logger.info("Call signup from wrapper client");
                return client.signup(_handle, req, info);
            }
            
        }, new TLoginResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TLoginResult login(final TLoginRequest req, TLoginInfo info)
    {
        return execute(new Call<TLoginResult>() {
            @Override
            public TLoginResult exec(MiotoCarService.Client client) throws Exception {
                return client.login(_handle, req, info);
            }
            
        }, new TLoginResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TLogoutResult logout(final long sessionId)
    {
        return execute(new Call<TLogoutResult>() {
            @Override
            public TLogoutResult exec(MiotoCarService.Client client) throws Exception {
                return client.logout(_handle, sessionId);
            }
            
        }, new TLogoutResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TSessionResult getSession(final long sessionId)
    {
        return execute(new Call<TSessionResult>() {
            @Override
            public TSessionResult exec(MiotoCarService.Client client) throws Exception {
                return client.getSession(_handle, sessionId);
            }
            
        }, new TSessionResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TUserResult getUser(final int userId)
    {
        return execute(new Call<TUserResult>() {
            @Override
            public TUserResult exec(MiotoCarService.Client client) throws Exception {
                return client.getUser(_handle, userId);
            }
            
        }, new TUserResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TUserResult getUserBySessionId(final long sessionId)
    {
        return execute(new Call<TUserResult>(){
            @Override
            public TUserResult exec(MiotoCarService.Client client) throws Exception {
                return client.getUserBySession(_handle, sessionId);
            }
            
        }, new TUserResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TUpdateUserResult updateUser(final TUser user)
    {
        return execute(new Call<TUpdateUserResult>() {
            @Override
            public TUpdateUserResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateUser(_handle, user);
            }
            
        }, new TUpdateUserResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
}
