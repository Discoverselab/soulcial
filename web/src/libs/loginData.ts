/**
 * Login Infor
 * Auth_Token：User Token
 */
import {
  local_storage, remove_local_storage_item
} from '../helper/localStorage';
import { jsonencode, jsondecode } from "../helper/base64";
interface Login {
  login_time: any,
  user_name: any,
  mobile: any,
  invitation_code: any,
  id_card: any,
  id: any,
  level: any,
  authToken: any,
  unique_code: any,
  node_type: any
}
export class UserData {
  set Auth_Token(value: any) {
    value ? local_storage('token', value) : remove_local_storage_item('token');
  }
  get Auth_Token() {
    return local_storage('token') || '';
  }
  get is_login() {
    return !!this.Auth_Token;
  }
  get is_login_recommend() {
    return !!this.loginRecommend;
  }
  get loginRecommend() {
    return local_storage('loginRecommend') || '';
  }
  set loginRecommend(value: any) {
    value ? local_storage('loginRecommend', value) : remove_local_storage_item('loginRecommend');
  }
  set loginTime(value: any) {
    value ? local_storage('loginTime', value) : remove_local_storage_item('loginTime');
  }
  get loginTime() {
    return local_storage('loginTime') || '';
  }
  set userId(value: any) {
    value ? local_storage('userId', value) : remove_local_storage_item('userId');
  }
  get userId() {
    return local_storage('userId') || '';
  }
  set userName(value: any) {
    value ? local_storage('userName', value) : remove_local_storage_item('userName');
  }
  get userName() {
    return (local_storage('userName') || '');
  }
  set inviteCode(value: any) {
    value ? local_storage('inviteCode', value) : remove_local_storage_item('inviteCode');
  }
  get inviteCode() {
    return (local_storage('inviteCode') || '');
  }
  set referInviteCode(value: any) {
    value ? local_storage('referInviteCode', value) : remove_local_storage_item('referInviteCode');
  }
  set uniqueCode(value: any) {
    value ? local_storage('uniqueCode', value) : remove_local_storage_item('uniqueCode');
  }
  get uniqueCode() {
    return (local_storage('uniqueCode') || '');
  }
  get referInviteCode() {
    return (local_storage('referInviteCode') || '');
  }
  set userLevel(value: any) {
    value ? local_storage('userLevel', value) : remove_local_storage_item('userLevel');
  }
  get userLevel() {
    return local_storage('userLevel') || '';
  }
  get idCard() {
    return local_storage('idCard') || '';
  }
  set idCard(value: any) {
    value ? local_storage('idCard', value) : remove_local_storage_item('idCard');
  }
  get userMobile() {
    return local_storage('userMobile') || '';
  }
  set userMobile(value: any) {
    value ? local_storage('userMobile', value) : remove_local_storage_item('userMobile');
  }
  get taskReported() {
    return local_storage('taskReported') || `{}`;
  }
  set taskReported(value: any) {
    value ? local_storage('taskReported', value) : remove_local_storage_item('taskReported');
  }
  get needReport() {
    return local_storage('needReport') || '';
  }
  set needReport(value: any) {
    value ? local_storage('needReport', value) : remove_local_storage_item('needReport');
  }
  get agentType() {
    return local_storage('agentType') || '';
  }
  set agentType(value: any) {
    value ? local_storage('agentType', value) : remove_local_storage_item('agentType');
  }
  out() {
    // this.Auth_Token = '';
    this.loginTime = '';
    this.userName = '';
    this.userId = '';
    this.userLevel = '';
    this.userMobile = '';
    this.inviteCode = '';
    this.idCard = '';
    this.Auth_Token = '';
    this.taskReported = '';
    this.needReport = '';
    this.uniqueCode = '';
  }


  in({
    login_time,
    user_name,
    mobile,
    invitation_code,
    id_card,
    id,
    level,
    authToken,
    unique_code,
    node_type,
  }: Login) {
    this.loginTime = login_time || new Date() as any * 1;
    this.userName = user_name;
    this.userId = id;
    this.userLevel = level;
    this.userMobile = mobile;
    this.inviteCode = invitation_code;
    this.idCard = id_card;
    this.Auth_Token = authToken;
    this.uniqueCode = unique_code;
  }
}

export default new UserData();