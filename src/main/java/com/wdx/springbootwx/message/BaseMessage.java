package com.wdx.springbootwx.message;

import lombok.Data;

/**
 * 回复消息的基类
 */
@Data
public class BaseMessage {
    protected String ToUserName;
    protected String FromUserName;
    protected long CreateTime;
    protected String MsgType;

}
