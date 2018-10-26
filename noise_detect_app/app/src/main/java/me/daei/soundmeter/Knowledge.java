package me.daei.soundmeter;

/**
 * Created by SunHongbin on 2018/10/23
 */
public class Knowledge {

/**
 * 首先你要知道Activity的四种状态：
 * Active/Runing 一个新 Activity 启动入栈后，它在屏幕最前端，处于栈的最顶端，此时它处于可见并可和用户交互的激活状态。
 * Paused 当 Activity 被另一个透明或者 Dialog 样式的 Activity 覆盖时的状态。此时它依然与窗口管理器保持连接，系统继续维护其内部状态，所以它仍然可见，但它已经失去了焦点故不可与用户交互。
 * Stoped 当 Activity 被另外一个 Activity 覆盖、失去焦点并不可见时处于 Stoped 状态。
 * Killed Activity 被系统杀死回收或者没有被启动时处于 Killed 状态。
 * protected void onStart() 该方法在 onCreate() 方法之后被调用，或者在 Activity 从 Stop 状态转换为 Active 状态时被调用，一般执行了onStart()后就执行onResume()。
 * protected void onResume() 在 Activity 从 Pause 状态转换到 Active 状态时被调用。
 */


/*    private Handler mHandler = new Handler() {
        @Override
        public void handleMessag(Message message) {
            handleMessage(message);
            //标记用户不退出状态
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判单用户是否点击的是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果isExit标记为false，提示用户再次按键
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //如果用户没有在2秒内再次按返回键的话，就发送消息标记用户为不退出状态
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                //exit
                finish();
                System.exit(0);
            }
        }
        return false;
    }*/

/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-MM-dd-HH-mm-ss");
    Date date = new Date(startTime);
    fileName =simpleDateFormat.format(date)+".txt";*/


}
