package com.xmg.p2p.base.domain;

import com.xmg.p2p.base.util.BitStatesUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Created by machao on 2017/8/30.
 */
@Setter@Getter
public class Userinfo extends BaseDomain {
    private int version;//版本号，用作乐观锁
    private long bitState = 0;//用户状态值
    private String realName;//用户实名值（冗余数据）
    private String idNumber;//用户身份证号（冗余数据）
    private String phoneNumber;//用户电话
    private String email;//电子邮箱
    private int score;//风控分数
    private Long realAuthId;//风控分数
    private SystemDictionaryItem incomeGrade;//收入
    private SystemDictionaryItem marriage;//婚姻情况
    private SystemDictionaryItem kidCount;//子女情况
    private SystemDictionaryItem educationBackground;//学历
    private SystemDictionaryItem houseCondition;//住房条件

    /**
     * 返回是否具有体现的流程
     * @return
     */
    public boolean gethasMoneyWithdrawProcess() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS);
    }
    /**
     * 返回用户是否已经绑定手机
     *
     * @return
     */
    public boolean getIsBindPhone() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_BIND_PHONE);
    }

    /**
     * 是否绑定银行卡
     * @return
     */
    public boolean getisBindBank() {
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_BANKINFO);
    }
    /**
     * 返回用户是否绑定邮箱
     * @return
     */
    public boolean getIsBindEmail(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_EMAIL);
    }

    /**
     * 返回是否填写用户基本信息
     * @return
     */
    public boolean getIsBasicInfo(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BASIC_INFO);
    }

    /**
     * 返回是否实名认证
     * @return
     */
    public boolean getIsRealAuth(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_REAL_AUTH);
    }

    /**
     * 返回是否是视频认证
     * @return
     */
    public boolean getIsVedioAuth(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_VEDIO_AUTH);
    }

    /**
     * 返回是否有正在借款流程
     * @return
     */
    public boolean getHasBidRequestProcess(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
    }
    /**
     * 添加状态码
     * @param stateCode
     */
    public void addState(Long stateCode) {
        this.bitState=BitStatesUtils.addState(this.bitState, stateCode);
    }

    /**
     * 移除状态码的方法
     * @param stateCode
     */
    public void removeState(Long stateCode) {
        this.bitState=BitStatesUtils.removeState(this.bitState, stateCode);
    }

    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     *
     * @param
     *
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @param
     * @return
     */
    public String getAnonymousIdNumber() {
        if (StringUtils.hasLength(idNumber)) {
            int len = idNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += idNumber.charAt(i);
                }
            }
            return replace;
        }
        return idNumber;
    }



}
