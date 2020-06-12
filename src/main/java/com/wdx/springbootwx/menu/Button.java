package com.wdx.springbootwx.menu;

import lombok.Data;

/**
 * 按钮
 */
@Data
public class Button {
    private String type;
    private String name;
    private Button[] sub_button;
}
