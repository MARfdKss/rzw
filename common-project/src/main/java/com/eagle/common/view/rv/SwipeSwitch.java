package com.eagle.common.view.rv;

public interface SwipeSwitch {
    boolean isMenuOpen();

    boolean isLeftMenuOpen();

    boolean isRightMenuOpen();

    boolean isCompleteOpen();

    boolean isLeftCompleteOpen();

    boolean isRightCompleteOpen();

    boolean isMenuOpenNotEqual();

    boolean isLeftMenuOpenNotEqual();

    boolean isRightMenuOpenNotEqual();

    void smoothOpenMenu();

    void smoothOpenLeftMenu();

    void smoothOpenRightMenu();

    void smoothOpenLeftMenu(int var1);

    void smoothOpenRightMenu(int var1);

    void smoothCloseMenu();

    void smoothCloseLeftMenu();

    void smoothCloseRightMenu();

    void smoothCloseMenu(int var1);
}
