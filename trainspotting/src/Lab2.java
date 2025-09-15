import TSim.*;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


// public class Lab1 {

//   public Lab1(int speed1, int speed2) {
//     TSimInterface tsi = TSimInterface.getInstance();
//     try {
//       tsi.setSpeed(1, speed1);
//       tsi.setSpeed(2, speed2);
//       tsi.setSwitch(17, 7, 2);

//       new Thread(() -> runTrain(1, speed1)).start();
//       new Thread(() -> runTrain(2, speed2)).start();
//     }
//     catch (CommandException e) {
//       e.printStackTrace();    // or only e.getMessage() for the error
//       System.exit(1);
//     }
//   }

//   private static Set<Point> STATION_SENSOR = new HashSet<>();
//   static{
//     STATION_SENSOR.add(new Point(13,3));
//     STATION_SENSOR.add(new Point(13,13));
//     STATION_SENSOR.add(new Point(13,5));
//     STATION_SENSOR.add(new Point(13,11));
//   }

//   private static Set<Point> Cross = new HashSet<>();
//   static{
//     Cross.add(new Point(8,5));
//     Cross.add(new Point(6,7));
//     Cross.add(new Point(10,7));
//     Cross.add(new Point(10,8));
//   }
//   private static Set<Point> Single_Right_Split = new HashSet<>();
//   static{
//     Single_Right_Split.add(new Point(19,7));
//     Single_Right_Split.add(new Point(17,9));
//   }

//   private static Point Single_Right_Split_Up = new Point(19,7);
//   private static Point Single_Right_Split_Down = new Point(17,9);

//   private static Point Single_Left_Split_Up = new Point(2,9);
//   private static Point Single_Left_Split_Down = new Point(1,11);


//   private static Point Station_A_Up_Entry = new Point(6,7);
//   private static Point Station_A_Down_Entry = new Point(8,5);

//   private static int Single_Right_Switch_Up_X = 17, Single_Right_Switch_Up_Y = 7;
//   private static int Single_Right_Switch_Down_X = 15, Single_Right_Switch_Down_Y = 9;
//   private static int Single_Left_Switch_Up_X = 4, Single_Left_Switch_Up_Y = 9;
//   private static int Single_Left_Switch_Down_X = 3, Single_Left_Switch_Down_Y = 11;



//   private static Set<Point> Mid_Upper_Right = new HashSet<>();
//   static{
//     Mid_Upper_Right.add(new Point(14,7));
//     Mid_Upper_Right.add(new Point(14,8));
//   };

//   private static Set<Point> Mid_Down_Right = new HashSet<>();
//   static{
//     Mid_Down_Right.add(new Point(12,9));
//     Mid_Down_Right.add(new Point(12,10));
//   };  
//   private static Set<Point> Mid_Down_Left = new HashSet<>();
//   static{
//     Mid_Down_Left.add(new Point(7,9));
//     Mid_Down_Left.add(new Point(7,10));
//   }; 

//   private static Set<Point> Station_B_Left = new HashSet<>();
//   static{
//     Station_B_Left.add(new Point(6,11));
//     Station_B_Left.add(new Point(6,13));
//   };  

//   private static Semaphore Cross_Boundary = new Semaphore(1, true);
//   private static Semaphore Single_Right = new Semaphore(1, true);
//   private static Semaphore Split_Upper_Up = new Semaphore(1, true);
//   private static Semaphore Split_Upper_Down = new Semaphore(1, true);
//   private static Semaphore Split_Mid_Up = new Semaphore(1, true);
//   private static Semaphore Split_Mid_Down = new Semaphore(1, true);
//   private static Semaphore Single_Left = new Semaphore(1, true);
//   private static Semaphore Split_Bottom_Down = new Semaphore(1, true);
//   private static Semaphore Split_Bottom_Up = new Semaphore(1, true);


//   private void runTrain(int id, int initalV) {
//     TSimInterface tsi = TSimInterface.getInstance();
//     int v = initalV;
//     Point laststopPoint = null;
//     boolean skipLastPoint = false;
//     boolean departing = true;
//     boolean onUp = false;
//     boolean inCross = false;
//     boolean inSingleRight = false;
//     boolean inUpperBranch = false;
//     boolean inMiddle = false;
//     boolean inSingleLeft = false;
//     boolean inBottomBranch = false;
//     try{
//       while (true) {
//         SensorEvent ev = tsi.getSensor(id);
//         if(ev.getStatus() != SensorEvent.ACTIVE) continue;

//         Point p = new Point(ev.getXpos(), ev.getYpos());
//         if(departing && STATION_SENSOR.contains(p)){
//           departing = false;
//           continue;
//         }
//         if(skipLastPoint && laststopPoint != null && p.equals(laststopPoint)){
//           skipLastPoint = false;
//           continue;
//         }
//         System.out.printf("T%d ACTIVE @(%d,%d) v=%d depart=%s",id, ev.getXpos(), ev.getYpos(), v, departing);

//         if(STATION_SENSOR.contains(p)) {
          
//           tsi.setSpeed(id, 0);
//           Thread.sleep(1000 + 20 * Math.abs(v));
//           laststopPoint = p;
//           skipLastPoint = true;
//           v = -v;
//           tsi.setSpeed(id, v);
//           continue;
//         }   
// //shang fang A zhan jin ru you ce dan gui hou
//         if(inUpperBranch && p.equals(Single_Right_Split_Up)){
//           if(onUp){
//             Split_Upper_Up.release();
//           }else{
//             Split_Upper_Down.release();
//           }
//           inUpperBranch = false;
//         }     

// //zhongduan jin ru you ce dan gui hou
//         if(inMiddle && !inSingleRight && p.equals(Single_Right_Split_Down)){
//           inSingleRight = true;
//           if(onUp){
//             Split_Mid_Up.release();
//           }else{
//             Split_Mid_Down.release();
//           }
//           inMiddle = false;
//           continue;
//           }
// // 


// // zuo ce dan gui jin ru zhong duan hou..
//         if(inSingleLeft && Mid_Down_Left.contains(p)){
//           Single_Left.release();
//           inSingleLeft = false;
//           continue;
//         }
// //zuo ce dan gui jin ru xia fang B zhan hou
//         if(inSingleLeft && Station_B_Left.contains(p)){
//           Single_Left.release();
//           inSingleLeft = false;
//           continue;
//         }
// //xia fang B zhan jin ru zuo ce dan gui hou
//         if(inBottomBranch && p.equals(Single_Left_Split_Down)){
//           if(onUp){
//             Split_Bottom_Up.release();
//           }else{
//             Split_Bottom_Down.release();
//           }
//           inBottomBranch = false;
//           continue;
//         }

// //you ce dan gui jin ru shang fang A zhan de switch kong zhi
//         if(p.equals(Single_Right_Split_Up)){
//           if (!inUpperBranch) {
//             if(Split_Upper_Up.tryAcquire()){
//               onUp = true;
//               inUpperBranch = true;
//               System.out.printf("onUp=%b, inBranch=%b%n, position=%s%n",  onUp, inUpperBranch, p);
//             }else if(Split_Upper_Down.tryAcquire()){
//               onUp = false;
//               inUpperBranch = true;
//               System.out.printf("onUp=%b, inBranch=%b%n, position=%s%n",  onUp, inUpperBranch, p);

//             }
//           }
//           int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//           tsi.setSwitch(Single_Right_Switch_Up_X, Single_Right_Switch_Up_Y, dir);
//           continue;
//         }



//         if(inSingleRight && p.equals(Single_Right_Split_Down)){
//           if (Split_Mid_Up.tryAcquire()){ 
//               onUp = true;
//             }else if (Split_Mid_Down.tryAcquire()) { 
//               onUp = false;
//             }
//           inMiddle = true;
//           int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//           tsi.setSwitch(Single_Right_Switch_Down_X, Single_Right_Switch_Down_Y, dir);
//         }

//         if (inSingleRight && Mid_Down_Right.contains(p)) {  
//           Single_Right.release();
//           inSingleRight = false;
//           continue; 
//         }

// // zhongduan zuo ce jin ru zuo ce dan gui ..
//         if (inMiddle && !inSingleLeft && Mid_Down_Left.contains(p)) {
       
//             if(!Single_Left.tryAcquire()){
//             tsi.setSpeed(id, 0);
//             Single_Left.acquire();
//             tsi.setSpeed(id, v);
//             }
//           int dir = onUp ? TSimInterface.SWITCH_LEFT : TSimInterface.SWITCH_RIGHT;
//           tsi.setSwitch(Single_Left_Switch_Up_X, Single_Left_Switch_Up_Y, dir);
//           continue;
//         }

        
// //zuo ce dan gui jin ru zhongduan ..
//         if(!inMiddle && p.equals(Single_Left_Split_Up)){
        
//             if(Split_Mid_Up.tryAcquire()){
//               onUp = true;
//             }else if(Split_Mid_Down.tryAcquire()){
//               onUp = false;
//             }
//           inMiddle = true;
//           int dir = onUp ? TSimInterface.SWITCH_LEFT : TSimInterface.SWITCH_RIGHT;
//           tsi.setSwitch(Single_Left_Switch_Up_X, Single_Left_Switch_Up_Y, dir);
//           continue;       
        
//         }
// //zuo ce dan gui jin xia fang B zhan
//         if(inSingleLeft && p.equals(Single_Left_Split_Down)){
//           if(!inBottomBranch){
//             if(Split_Bottom_Up.tryAcquire()){
//               onUp = true;
//               inBottomBranch = true;
//             }else if(Split_Bottom_Down.tryAcquire()){
//               onUp = false;
//               inBottomBranch = true;
//             }
//           }
//           int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//           tsi.setSwitch(Single_Left_Switch_Down_X, Single_Left_Switch_Down_Y, dir);
//           continue; 
//         }


// // shang fang A zhan jin ru you ce dan gui
//         if (inUpperBranch && Mid_Upper_Right.contains(p)){
//           if(!Single_Right.tryAcquire()){
//             tsi.setSpeed(id, 0);
//             Single_Right.acquire();
//             tsi.setSpeed(id, v);
//             }
//             Single_Right.acquire();
//             inSingleRight = true;
//             int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//             tsi.setSwitch(Single_Right_Switch_Up_X, Single_Right_Switch_Up_Y, dir);
//             continue;
//           }
// // you ce dan gui jin ru shang fang A zhan
//           if(inSingleRight && p.equals(Single_Right_Split_Up)){
//             if (Split_Upper_Up.tryAcquire()) {
//               onUp=true;

//             }else if(Split_Upper_Down.tryAcquire()){
//               onUp = false;
//             }
//             inUpperBranch = true;
//             int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//             tsi.setSwitch(Single_Right_Switch_Up_X, Single_Right_Switch_Up_Y, dir);
//           }
// //zhongduan jin ru you ce dan gui ..
//         if (inMiddle && !inSingleRight && Mid_Down_Right.contains(p)) {
//             System.out.printf("inMiddle=%b inSingleRight=%b onUp=%b%n", inMiddle, inSingleRight, onUp);
//           if(!Single_Right.tryAcquire()){
//             tsi.setSpeed(id, 0);
//             Single_Right.acquire();
//             tsi.setSpeed(id, v);
//           }
//           int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//           tsi.setSwitch(Single_Right_Switch_Down_X, Single_Right_Switch_Down_Y, dir);
//           continue;
//         }





// //xia fang B zhan jin ru zuo ce dan gui
//         if(inBottomBranch && Station_B_Left.contains(p)){
//           if(Single_Left.tryAcquire()){
//             int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//             tsi.setSwitch(Single_Left_Switch_Down_X, Single_Left_Switch_Down_Y, dir);
//             continue;  
//           }
//           tsi.setSpeed(id, 0);
//           Single_Left.acquire();
//           int dir = onUp ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT;
//           tsi.setSwitch(Single_Left_Switch_Down_X, Single_Left_Switch_Down_Y, dir);
//           tsi.setSpeed(id, v);
//           continue;
//         }

// // //ru guo zhong duan zuo ce yao jin ru zuo ce dan gui, ru guo dan gui nei you che, ze ting che deng dai .. ke neng bu yao le
// //         if(!inSingleLeft && Mid_Down_Left.contains(p)){
// //           if(!Single_Left.tryAcquire()){
// //             tsi.setSpeed(id, 0);
// //             Single_Left.acquire();
// //             tsi.setSpeed(id, v);
// //           }
// //           inSingleLeft = true;
// //           int dir = onUp ? TSimInterface.SWITCH_LEFT : TSimInterface.SWITCH_RIGHT;
// //           tsi.setSwitch(Single_Left_Switch_Up_X, Single_Left_Switch_Up_Y, dir);  

// //         }


        
//         // //A station before cross road to lock the trail
//         // if(p.equals(Station_A_Up_Entry)){
//         //   if(!Split_Upper_Up.tryAcquire()){
//         //     Split_Upper_Up.acquire();
//         //   }
//         //   onUp = true;
//         //   inBranch = true;
//         //   continue;
//         // }

//         // if (p.equals(Station_A_Down_Entry)) {
//         //   if(!Split_Upper_Down.tryAcquire()){
//         //     Split_Upper_Down.acquire();
//         //   }
//         //   onUp = false;
//         //   inBranch = true;
//         //   continue;
          
//         // }

        
//         // if(Cross.contains(p)){
//         //   if(!inCross){
//         //     if (!Cross_Boundary.tryAcquire()) {
//         //       tsi.setSpeed(id, 0);
//         //       Cross_Boundary.acquire();

//         //       tsi.setSpeed(id, v);
              
//         //     }
//         //     inCross = true;
//         //   }else{
            
//         //       inCross = false;
//         //       Cross_Boundary.release(); 
            

//         //   }
//         //   continue;
//         // }
 
//       }
//     } 
    
//     catch (CommandException | InterruptedException e) {
//       System.err.printf("T%d CommandException: %s%n", id, e.getMessage());
//       e.printStackTrace();
//       //System.exit(1);
//     }
//   }
  
      

// }



public class Lab2 {

    public Lab2(int speed1, int speed2) {
        TSimInterface tsi = TSimInterface.getInstance();
        tsi.setDebug(false);


        try {
            tsi.setSpeed(1, speed1);
            tsi.setSpeed(2, speed2);
            tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);


        } catch (CommandException e) {
            e.printStackTrace();    // or only e.getMessage() for the error
            System.exit(1);
        }

        Semaphore fourWay = new Semaphore(1);
        Semaphore black = new Semaphore(0);
        Semaphore red = new Semaphore(1);
        Semaphore green = new Semaphore(1);
        Semaphore purple = new Semaphore(1);
        Semaphore blue = new Semaphore(1);
        Semaphore orange = new Semaphore(1);
        Semaphore pink = new Semaphore(0);
        Semaphore teal = new Semaphore(1);


        //tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);


        class Train implements Runnable {

            int id;
            int speed;
            // True = north
            boolean direction;

            public Train(int id, int speed, boolean dir) {
                this.id = id;
                this.speed = speed;
                this.direction = dir;
            }
            int v = speed;
            @Override
            public void run() {
                try {
                    while (!!!false) {
                        SensorEvent se = tsi.getSensor(id);

                        // Black past Fourway
                        if (se.getXpos() == 6 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                                fourWay.release();
                            }
                        }

                        // Black east
                        if (se.getXpos() == 14 && se.getYpos() == 7 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                black.release();
                                System.out.println("Black released! - " + black.availablePermits());
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                                // to North
                            }else{
                               green.release();
                            }
                        }

                        // Red north fourway
                        if (se.getXpos() == 8 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                               fourWay.release();
                            }
                        }

                        // Red south fourway
                        if (se.getXpos() == 9 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                               fourWay.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Green east fourway
                        if (se.getXpos() == 10 && se.getYpos() == 7 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                fourWay.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                fourWay.acquire();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Red east Tcrossing
                        if (se.getXpos() == 16 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                red.release();
                                System.out.println("Red released! - " + red.availablePermits());


                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                                // to North
                            } else {
                                green.release();

                            }
                        }


                        // Green east
                        if (se.getXpos() == 19 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE && direction) {
                            // to South
                            tsi.setSpeed(id, 0);


                            if (red.tryAcquire()) {
                                System.out.println("Red acquired! - " + red.availablePermits());

                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);

                            } else {
                                black.acquire();
                                System.out.println("Black acquired! - " + black.availablePermits());


                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                            }
                            tsi.setSpeed(id, speed);

                        }

                        // Green south
                        if (se.getXpos() == 17 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (purple.tryAcquire()) {
                                    tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
                                } else {
                                    blue.acquire();
                                    tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
                                }
                                tsi.setSpeed(id, speed);
                                // to North
                            }
                        }

                        // Purple east
                        if (se.getXpos() == 13 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                green.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                purple.release();
                                tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);

                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Blue east
                        if (se.getXpos() == 13 && se.getYpos() == 10 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                green.release();
                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                green.acquire();
                                blue.release();
                                tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);

                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Purple west
                        if (se.getXpos() == 6 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                orange.acquire();

                                tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                                purple.release();
                                tsi.setSpeed(id, speed);
                            } else {
                                orange.release();

                            }
                        }

                        // Blue west
                        if (se.getXpos() == 6 && se.getYpos() == 10 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                orange.acquire();

                                tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                                blue.release();
                            } else {
                                orange.release();

                            }
                        }

                        // Orange
                        if (se.getXpos() == 2 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (pink.tryAcquire()) {


                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                    tsi.setSpeed(id, speed);
                                } else {
                                    teal.acquire();

                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                    tsi.setSpeed(id, speed);
                                }
                                tsi.setSpeed(id, speed);
                            } else {
                                tsi.setSpeed(id, 0);
                                if (purple.tryAcquire()) {
                                    tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                                    tsi.setSpeed(id, speed);
                                } else if (blue.tryAcquire()) {

                                    tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                                    tsi.setSpeed(id, speed);
                                }
                            }


                        }

                        // Pink west
                        if (se.getXpos() == 5 && se.getYpos() == 11 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                orange.release();

                                // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                orange.acquire();

                                pink.release();
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Teal west
                        if (se.getXpos() == 4 && se.getYpos() == 13 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                orange.release();

                                // to North
                            } else {

                                tsi.setSpeed(id, 0);
                                orange.acquire();
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                teal.release();

                                tsi.setSpeed(id, speed);
                            }
                        }

                        /// North Station
                        if (se.getXpos() == 13 && (se.getYpos() == 5 || se.getYpos() == 3) && direction && se.getStatus() == SensorEvent.ACTIVE) {
                            tsi.setSpeed(id, 0);
                            Thread.sleep(1000 + 20 * Math.abs(v));
                            speed = -speed;
                            tsi.setSpeed(id, speed);
                            direction = !direction;
                        }


                        /// South Station
                        if (se.getXpos() == 13 && (se.getYpos() == 11 || se.getYpos() == 13) && !direction && se.getStatus() == SensorEvent.ACTIVE) {
                            tsi.setSpeed(id, 0);
                            Thread.sleep(1000 + 20 * Math.abs(v));
                            speed = -speed;
                            tsi.setSpeed(id, speed);
                            direction = !direction;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        Thread train2 = new Thread(new Train(2, speed2, true));
        train2.start();
        Thread train1 = new Thread(new Train(1, speed1, false));
        train1.start();


    }

}