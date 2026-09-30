/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wrapper;

import error.Err;
import thrift.MiotoCarService;
import thrift.OpHandle;
import thrift.TCarBrand;
import thrift.TCarBrandResult;
import thrift.TDistrict;
import thrift.TDistrictResult;
import thrift.TFeature;
import thrift.TFeatureResult;
import thrift.TFeePolicy;
import thrift.TFeePolicyResult;
import thrift.TFeedBack;
import thrift.TFeedBackResult;
import thrift.TListCarBrandResult;
import thrift.TListDistrictResult;
import thrift.TListFeatureResult;
import thrift.TListFeePolicyResult;
import thrift.TListFeedBackResult;
import thrift.TListProvinceResult;
import thrift.TListVoucherResult;
import thrift.TLoginInfo;
import thrift.TLoginRequest;
import thrift.TLoginResult;
import thrift.TLogoutResult;
import thrift.TProvince;
import thrift.TProvinceResult;
import thrift.TSessionResult;
import thrift.TSignUpRequest;
import thrift.TUpdateUserResult;
import thrift.TUser;
import thrift.TUserResult;
import thrift.TVoucher;
import thrift.TVoucherResult;
import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

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
    
    // ===================== Auth / User =====================
    
    public TLoginResult signup(final TSignUpRequest req, TLoginInfo info)
    {
        return execute(new Call<TLoginResult>() {
            @Override
            public TLoginResult exec(MiotoCarService.Client client) throws Exception {
                _Logger.info("Call signup from wrapper client");
                return (TLoginResult) client.signup(_handle, req, info);
            }
            
        }, new TLoginResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TLoginResult login(final TLoginRequest req, TLoginInfo info)
    {
        return execute(new Call<TLoginResult>() {
            @Override
            public TLoginResult exec(MiotoCarService.Client client) throws Exception {
                return (TLoginResult) client.login(_handle, req, info);
            }
            
        }, new TLoginResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TLogoutResult logout(final long sessionId)
    {
        return execute(new Call<TLogoutResult>() {
            @Override
            public TLogoutResult exec(MiotoCarService.Client client) throws Exception {
                return (TLogoutResult) client.logout(_handle, sessionId);
            }
            
        }, new TLogoutResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TSessionResult getSession(final long sessionId)
    {
        return execute(new Call<TSessionResult>() {
            @Override
            public TSessionResult exec(MiotoCarService.Client client) throws Exception {
                return (TSessionResult) client.getSession(_handle, sessionId);
            }
            
        }, new TSessionResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TUserResult getUser(final int userId)
    {
        return execute(new Call<TUserResult>() {
            @Override
            public TUserResult exec(MiotoCarService.Client client) throws Exception {
                return (TUserResult) client.getUser(_handle, userId);
            }
            
        }, new TUserResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TUserResult getUserBySessionId(final long sessionId, final TLoginInfo info)
    {
        return execute(new Call<TUserResult>(){
            @Override
            public TUserResult exec(MiotoCarService.Client client) throws Exception {
                return (TUserResult) client.getUserBySession(_handle, sessionId, info);
            }
            
        }, new TUserResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TUpdateUserResult updateUser(final TUser user)
    {
        return execute(new Call<TUpdateUserResult>() {
            @Override
            public TUpdateUserResult exec(MiotoCarService.Client client) throws Exception {
                return (TUpdateUserResult) client.updateUser(_handle, user);
            }
            
        }, new TUpdateUserResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== FeePolicy =====================
    
    public TFeePolicyResult createFeePolicy(final TFeePolicy feePolicy)
    {
        return execute(new Call<TFeePolicyResult>() {
            @Override
            public TFeePolicyResult exec(MiotoCarService.Client client) throws Exception {
                return client.createFeePolicy(_handle, feePolicy);
            }
            
        }, new TFeePolicyResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeePolicyResult updateFeePolicy(final TFeePolicy feePolicy)
    {
        return execute(new Call<TFeePolicyResult>() {
            @Override
            public TFeePolicyResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateFeePolicy(_handle, feePolicy);
            }
            
        }, new TFeePolicyResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListFeePolicyResult getFeePolicy(final String name, final int count, final int offset)
    {
        return execute(new Call<TListFeePolicyResult>() {
            @Override
            public TListFeePolicyResult exec(MiotoCarService.Client client) throws Exception {
                return client.getFeePolicy(_handle, name, count, offset);
            }
            
        }, new TListFeePolicyResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeePolicyResult getFeePolicyById(final int feePolicyId)
    {
        return execute(new Call<TFeePolicyResult>() {
            @Override
            public TFeePolicyResult exec(MiotoCarService.Client client) throws Exception {
                return client.getFeePolicyById(_handle, feePolicyId);
            }
            
        }, new TFeePolicyResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeePolicyResult deleteFeePolicy(final int feePolicyId)
    {
        return execute(new Call<TFeePolicyResult>() {
            @Override
            public TFeePolicyResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteFeePolicy(_handle, feePolicyId);
            }
            
        }, new TFeePolicyResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== Province =====================
    
    public TProvinceResult createProvince(final TProvince province)
    {
        return execute(new Call<TProvinceResult>() {
            @Override
            public TProvinceResult exec(MiotoCarService.Client client) throws Exception {
                return client.createProvince(_handle, province);
            }
            
        }, new TProvinceResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TProvinceResult updateProvince(final TProvince province)
    {
        return execute(new Call<TProvinceResult>() {
            @Override
            public TProvinceResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateProvince(_handle, province);
            }
            
        }, new TProvinceResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListProvinceResult getProvince(final String provinceName, final int count, final int offset)
    {
        return execute(new Call<TListProvinceResult>() {
            @Override
            public TListProvinceResult exec(MiotoCarService.Client client) throws Exception {
                return client.getProvince(_handle, provinceName, count, offset);
            }
            
        }, new TListProvinceResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TProvinceResult getProvinceById(final int provinceId)
    {
        return execute(new Call<TProvinceResult>() {
            @Override
            public TProvinceResult exec(MiotoCarService.Client client) throws Exception {
                return client.getProvinceById(_handle, provinceId);
            }
            
        }, new TProvinceResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TProvinceResult deleteProvince(final int provinceId)
    {
        return execute(new Call<TProvinceResult>() {
            @Override
            public TProvinceResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteProvince(_handle, provinceId);
            }
            
        }, new TProvinceResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== District =====================
    
    public TDistrictResult createDistrict(final TDistrict district)
    {
        return execute(new Call<TDistrictResult>() {
            @Override
            public TDistrictResult exec(MiotoCarService.Client client) throws Exception {
                return client.createDistrict(_handle, district);
            }
            
        }, new TDistrictResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TDistrictResult updateDistrict(final TDistrict district)
    {
        return execute(new Call<TDistrictResult>() {
            @Override
            public TDistrictResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateDistrict(_handle, district);
            }
            
        }, new TDistrictResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListDistrictResult getDistrict(final int provinceId, final int count, final int offset)
    {
        return execute(new Call<TListDistrictResult>() {
            @Override
            public TListDistrictResult exec(MiotoCarService.Client client) throws Exception {
                return client.getDistrict(_handle, provinceId, count, offset);
            }
            
        }, new TListDistrictResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TDistrictResult deleteDistrict(final int districtId)
    {
        return execute(new Call<TDistrictResult>() {
            @Override
            public TDistrictResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteDistrict(_handle, districtId);
            }
            
        }, new TDistrictResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== CarBrand =====================
    
    public TCarBrandResult createCarBrand(final TCarBrand carBrand)
    {
        return execute(new Call<TCarBrandResult>() {
            @Override
            public TCarBrandResult exec(MiotoCarService.Client client) throws Exception {
                return client.createCarBrand(_handle, carBrand);
            }
            
        }, new TCarBrandResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TCarBrandResult updateCarBrand(final TCarBrand carBrand)
    {
        return execute(new Call<TCarBrandResult>() {
            @Override
            public TCarBrandResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateCarBrand(_handle, carBrand);
            }
            
        }, new TCarBrandResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListCarBrandResult getCarBrand(final String nameBrand, final int count, final int offset)
    {
        return execute(new Call<TListCarBrandResult>() {
            @Override
            public TListCarBrandResult exec(MiotoCarService.Client client) throws Exception {
                return client.getCarBrand(_handle, nameBrand, count, offset);
            }
            
        }, new TListCarBrandResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TCarBrandResult getCarBrandById(final int carBrandId)
    {
        return execute(new Call<TCarBrandResult>() {
            @Override
            public TCarBrandResult exec(MiotoCarService.Client client) throws Exception {
                return client.getCarBrandById(_handle, carBrandId);
            }
            
        }, new TCarBrandResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TCarBrandResult deleteCarBrand(final int carBrandId)
    {
        return execute(new Call<TCarBrandResult>() {
            @Override
            public TCarBrandResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteCarBrand(_handle, carBrandId);
            }
            
        }, new TCarBrandResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== Feature =====================
    
    public TFeatureResult createFeature(final TFeature feature)
    {
        return execute(new Call<TFeatureResult>() {
            @Override
            public TFeatureResult exec(MiotoCarService.Client client) throws Exception {
                return client.createFeature(_handle, feature);
            }
            
        }, new TFeatureResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeatureResult updateFeature(final TFeature feature)
    {
        return execute(new Call<TFeatureResult>() {
            @Override
            public TFeatureResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateFeature(_handle, feature);
            }
            
        }, new TFeatureResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListFeatureResult getFeature(final String nameFeature, final int count, final int offset)
    {
        return execute(new Call<TListFeatureResult>() {
            @Override
            public TListFeatureResult exec(MiotoCarService.Client client) throws Exception {
                return client.getFeature(_handle, nameFeature, count, offset);
            }
            
        }, new TListFeatureResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeatureResult getFeatureById(final int featureId)
    {
        return execute(new Call<TFeatureResult>() {
            @Override
            public TFeatureResult exec(MiotoCarService.Client client) throws Exception {
                return client.getFeatureById(_handle, featureId);
            }
            
        }, new TFeatureResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeatureResult deleteFeature(final int featureId)
    {
        return execute(new Call<TFeatureResult>() {
            @Override
            public TFeatureResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteFeature(_handle, featureId);
            }
            
        }, new TFeatureResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== FeedBack =====================
    
    public TFeedBackResult createFeedBack(final TFeedBack feedback)
    {
        return execute(new Call<TFeedBackResult>() {
            @Override
            public TFeedBackResult exec(MiotoCarService.Client client) throws Exception {
                return client.createFeedBack(_handle, feedback);
            }
            
        }, new TFeedBackResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeedBackResult updateFeedBack(final TFeedBack feedback)
    {
        return execute(new Call<TFeedBackResult>() {
            @Override
            public TFeedBackResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateFeedBack(_handle, feedback);
            }
            
        }, new TFeedBackResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListFeedBackResult getFeedBack(final int receiverId, final int count, final int offset)
    {
        return execute(new Call<TListFeedBackResult>() {
            @Override
            public TListFeedBackResult exec(MiotoCarService.Client client) throws Exception {
                return client.getFeedBack(_handle, receiverId, count, offset);
            }
            
        }, new TListFeedBackResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TFeedBackResult deleteFeedBack(final int feedBackId)
    {
        return execute(new Call<TFeedBackResult>() {
            @Override
            public TFeedBackResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteFeedBack(_handle, feedBackId);
            }
            
        }, new TFeedBackResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    // ===================== Voucher =====================
    
    public TVoucherResult createVoucher(final TVoucher voucher)
    {
        return execute(new Call<TVoucherResult>() {
            @Override
            public TVoucherResult exec(MiotoCarService.Client client) throws Exception {
                return client.createVoucher(_handle, voucher);
            }
            
        }, new TVoucherResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TVoucherResult updateVoucher(final TVoucher voucher)
    {
        return execute(new Call<TVoucherResult>() {
            @Override
            public TVoucherResult exec(MiotoCarService.Client client) throws Exception {
                return client.updateVoucher(_handle, voucher);
            }
            
        }, new TVoucherResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TListVoucherResult getVoucher(final String title, final int count, final int offset, final String code)
    {
        return execute(new Call<TListVoucherResult>() {
            @Override
            public TListVoucherResult exec(MiotoCarService.Client client) throws Exception {
                return client.getVoucher(_handle, title, count, offset, code);
            }
            
        }, new TListVoucherResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TVoucherResult getVoucherById(final int voucherId)
    {
        return execute(new Call<TVoucherResult>() {
            @Override
            public TVoucherResult exec(MiotoCarService.Client client) throws Exception {
                return client.getVoucherById(_handle, voucherId);
            }
            
        }, new TVoucherResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
    public TVoucherResult deleteVoucher(final int voucherId)
    {
        return execute(new Call<TVoucherResult>() {
            @Override
            public TVoucherResult exec(MiotoCarService.Client client) throws Exception {
                return client.deleteVoucher(_handle, voucherId);
            }
            
        }, new TVoucherResult(Err.NO_CONNECTION, "Lỗi kết nối mạng"));
    }
    
}