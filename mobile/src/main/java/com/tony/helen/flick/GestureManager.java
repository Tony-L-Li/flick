package com.tony.helen.flick;

import android.content.Context;
import android.util.Log;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;

import java.util.ArrayList;

/**
 * Created by tli on 2015-11-28.
 */
public class GestureManager {
    public enum Gesture {
        FRONT_OUT ("front wave out", 0),
        FRONT_IN ("front wave in", 1),
        FRONT_FIST("front fist", 2),
        FRONT_SPREAD("front fingers spread", 3),
        DOWN_OUT ("lower wave out", 4),
        DOWN_IN ("lower wave in", 5),
        DOWN_FIST("lower fist", 6),
        DOWN_SPREAD("lower fingers spread", 7),
        LOCK("locked", 13),
        UNLOCK("unlocked", 14),
        FIST("fist", 15);

        private final String action;
        private final int index;

        Gesture(String action, int index) {
            this.action = action;
            this.index = index;
        }

        public String action() {
            return action;
        }

        public int index() {
            return index;
        }
    }

    Hub hub;
    GestureListener listener;
    private String[] gesturePhrases;
    private static GestureManager instance = null;
    private Float[][] gyroCalibrations;
    private boolean isCalibrated;
    private Float roll, pitch, yaw;
    private int currentOrientation;

    protected GestureManager(Context context) {
        int currentOrientation = 0;
        roll = 0f;
        pitch = 0f;
        yaw = 0f;
        isCalibrated = false;
        gyroCalibrations = new Float[2][3];
        gesturePhrases = new String[8];
        listener = null;

        gesturePhrases[0] = "Hello";
        gesturePhrases[1] = "My name is Tony";
        gesturePhrases[2] = "Where is the washroom";
        gesturePhrases[3] = "Excuse me";
        gesturePhrases[4] = "Can I get a coffee please";
        gesturePhrases[5] = "When is the next bus coming";
        gesturePhrases[6] = "Someone help me";
        gesturePhrases[7] = "Bill please";

        hub = Hub.getInstance();
        if (!hub.init(context, context.getPackageName())) {
            return;
        }
        hub.attachToAdjacentMyo();
        hub.addListener(mListener);
    }

    public static GestureManager getInstance(Context context) {
        if(instance == null) {
            instance = new GestureManager(context);
        }
        return instance;
    }

    public String getPhrase(Gesture gesture) {
        return gesturePhrases[gesture.index()];
    }

    public void setPhrase(Gesture gesture, String phrase) {
        gesturePhrases[gesture.index()] = phrase;
    }

    public void setGyroCalibrations(Float[][] calibrations) {
        gyroCalibrations = calibrations;
        isCalibrated = true;
    }

    public Float[] getGyro() {
        return new Float[]{roll, pitch, yaw};
    }

    public void setListener(GestureListener listener) {
        this.listener = listener;
    }
    public interface GestureListener {
        void onNewGesture(Gesture newGesture);
        void onConnected();
    }

    public void setGyro(float roll, float pitch, float yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            // Set the text color of the text view to cyan when a Myo connects.
            listener.onConnected();
        }
        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            // Set the text color of the text view to red when a Myo disconnects.

        }
        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        }
        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
        }
        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {
            Log.d("myo", "unlock");
            hub.setLockingPolicy(Hub.LockingPolicy.NONE);
            listener.onNewGesture(Gesture.UNLOCK);
        }
        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {
            Log.d("myo", "lock");
            listener.onNewGesture(Gesture.LOCK);
        }
        // onOrientationData() is called whenever a Myo provides its current orientation,
        // represented as a quaternion.
        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            //in : 10 -10 25
            //down : 120 -85 160
            //natural : 0 -20 -155

            // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            //Log.d("myo", String.format("%s %s %s", roll, pitch, yaw));
            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                roll *= -1;
                pitch *= -1;
            }
            GestureManager.getInstance(null).setGyro(roll, pitch, yaw);

            int threshold = 30;
            if (isCalibrated) {
                for (int i = 0; i < 2; i++) {
                    if (Math.abs(roll - gyroCalibrations[i][0]) < threshold &&
                            Math.abs(pitch - gyroCalibrations[i][1]) < threshold &&
                            Math.abs(yaw - gyroCalibrations[i][2]) < threshold) {
                        currentOrientation = i;
                        //Log.d("myo", Integer.toString(i));
                        break;
                    }
                }
            }
        }

        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            if (isCalibrated) {
                switch (pose) {
                    case UNKNOWN:
                        break;
                    case REST:
                    case DOUBLE_TAP:
                        switch (myo.getArm()) {
                            case LEFT:
                                break;
                            case RIGHT:
                                break;
                        }
                        break;
                    case FIST:
                        if (currentOrientation == 0) {
                            listener.onNewGesture(Gesture.FRONT_FIST);
                        } else {
                            listener.onNewGesture(Gesture.DOWN_FIST);
                        }
                        break;
                    case WAVE_IN:
                        if (currentOrientation == 0) {
                            listener.onNewGesture(Gesture.FRONT_IN);
                        } else {
                            listener.onNewGesture(Gesture.DOWN_IN);
                        }
                        break;
                    case WAVE_OUT:
                        if (currentOrientation == 0) {
                            listener.onNewGesture(Gesture.FRONT_OUT);
                        } else {
                            listener.onNewGesture(Gesture.DOWN_OUT);
                        }
                        break;
                    case FINGERS_SPREAD:
                        if (currentOrientation == 0) {
                            listener.onNewGesture(Gesture.FRONT_SPREAD);
                        } else {
                            listener.onNewGesture(Gesture.DOWN_SPREAD);
                        }
                        break;
                }
            } else {
                if (pose == Pose.FIST) {
                    listener.onNewGesture(Gesture.FIST);
                }
            }
            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
                // hold the poses without the Myo becoming locked.
                myo.unlock(Myo.UnlockType.HOLD);
                // Notify the Myo that the pose has resulted in an action, in this case changing
                // the text on the screen. The Myo will vibrate.
                myo.notifyUserAction();
            } else {
                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
                // stay unlocked while poses are being performed, but lock after inactivity.
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }
    };

    public void unlock() {
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
    }

    public void lock() {
        hub.setLockingPolicy(Hub.LockingPolicy.STANDARD);
//
//        ArrayList<Myo> myos = hub.getConnectedDevices();
//        if (myos.size() > 0) {
//            myos.get(0).lock();
//        }
    }

    public void destroyListener() {
        hub.removeListener(mListener);
    }

    public void finishHub() {
        hub.shutdown();
    }
}
