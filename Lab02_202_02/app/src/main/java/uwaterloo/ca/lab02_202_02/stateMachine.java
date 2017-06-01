package uwaterloo.ca.lab02_202_02;

import static uwaterloo.ca.lab02_202_02.stateMachine.states.DOWN;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.LD;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.LEFT;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.LL;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.LR;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.LU;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.RIGHT;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.UP;
import static uwaterloo.ca.lab02_202_02.stateMachine.states.WAITING;

/**
 * Created by Joseph Grooth on 2017-05-31.
 */

//creates state machine
public class stateMachine {
    //creates and enumerates the states
    enum states{LR, LL, LU,LD, RIGHT, LEFT, UP, DOWN, WAITING};

    //creates a state variable and intializes it to waiting
    static states currentState= WAITING;

    //creates an accelerometer sensor listener that is analyzed for gestures
    static public accelSensorEventListener accel=null;

    //Creates a counter to see how long the state machine has been in the waiting state
    static int waitingTime=0;

    //creates counter that waits for the second part of a gesture
    static int counter=0;

    //returns the current state since it is private and can't be accessed outside the class
    static states getCurrentState(){
        return (currentState);
    }




    //Used to update the state of the state machine
    static states checkChange(){

        //checks to see if the state is waiting and if so updates the counter that records how long the apps been in the waiting state
        if(currentState==WAITING){
            waitingTime++;
        }
        // if not waiting sets the waiting counter back to zero
        else{
            waitingTime=0;
        }

        //Checks to see if the app has been waitng for the rest of a gesture for too long and if so sets it back to the netral waiting state
        if(counter>15){
            currentState=WAITING;
            counter=0;
        }

        //checks for a min values "above" a threshold and changes the state to look for the rest of the gesture. Lets call this part 1
        else if ((currentState==WAITING) &&(accel.prevOneHundred[1][0]<accel.prevOneHundred[0][0]) &&(accel.prevOneHundred[1][0]<accel.prevOneHundred[2][0])&&(accel.prevOneHundred[1][0]<-7)){
            currentState=LL;
            counter=0;
        }
        //check to see if looking for a specific gesture and if it is and meets gesture changes state to the gesture. Lets call this part 2
        else if ((currentState==LL)&&(accel.prevOneHundred[1][0]>accel.prevOneHundred[0][0]) &&(accel.prevOneHundred[1][0]>accel.prevOneHundred[2][0])&&(accel.prevOneHundred[1][0]>3)){
            currentState=LEFT;
        }
        //Like part 1
        else if ((currentState==WAITING) &&(accel.prevOneHundred[1][0]>accel.prevOneHundred[0][0]) &&(accel.prevOneHundred[1][0]>accel.prevOneHundred[2][0])&&(accel.prevOneHundred[1][0]>7)){
            currentState=LR;
            counter=0;
        }
        //Like part 2
        else if ((currentState==LR)&&(accel.prevOneHundred[1][0]<accel.prevOneHundred[0][0]) &&(accel.prevOneHundred[1][0]<accel.prevOneHundred[2][0])&&(accel.prevOneHundred[1][0]<-3)){
            currentState=RIGHT;
        }
        //Like part 1
        else if((currentState==WAITING)&&(accel.prevOneHundred[1][1]>3)&&(accel.prevOneHundred[1][1]>accel.prevOneHundred[2][1])&&(accel.prevOneHundred[1][1]>accel.prevOneHundred[0][1])){
            currentState=LU;
            counter=0;
        }
        //Like part 2
        else if((currentState==LU)&&(accel.prevOneHundred[1][1]<-1)&&(accel.prevOneHundred[1][1]<accel.prevOneHundred[2][1])&&(accel.prevOneHundred[1][1]<accel.prevOneHundred[0][1])){
            currentState=UP;
        }
        //Like part 1
        else if((currentState==WAITING)&&(accel.prevOneHundred[1][1]<-3)&&(accel.prevOneHundred[1][1]<accel.prevOneHundred[2][1])&&(accel.prevOneHundred[1][1]<accel.prevOneHundred[0][1])){
            currentState=LD;
            counter=0;
        }
        //Like part 2
        else if((currentState==LD)&&(accel.prevOneHundred[1][1]>1)&&(accel.prevOneHundred[1][1]>accel.prevOneHundred[2][1])&&(accel.prevOneHundred[1][1]>accel.prevOneHundred[0][1])){
            currentState=DOWN;
        }
        ////////////
        return currentState;
    }

}
