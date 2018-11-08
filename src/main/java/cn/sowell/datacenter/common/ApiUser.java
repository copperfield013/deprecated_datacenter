package cn.sowell.datacenter.common;

import org.springframework.security.core.userdetails.UserDetails;

import cn.sowell.copframe.common.UserIdentifier;

public interface ApiUser extends UserDetails, UserIdentifier{

}
