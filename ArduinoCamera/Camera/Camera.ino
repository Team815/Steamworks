//
// begin license header
//
// This file is part of Pixy CMUcam5 or "Pixy" for short
//
// All Pixy source code is provided under the terms of the
// GNU General Public License v2 (http://www.gnu.org/licenses/gpl-2.0.html).
// Those wishing to use Pixy source code, software and/or
// technologies under different licensing terms should contact us at
// cmucam@cs.cmu.edu. Such licensing terms are available for
// all portions of the Pixy codebase presented here.
//
// end license header
//
// This sketch is a good place to start if you're just getting started with 
// Pixy and Arduino.  This program simply prints the detected object blocks 
// (including color codes) through the serial console.  It uses the Arduino's 
// ICSP port.  For more information go here:
//
// http://cmucam.org/projects/cmucam5/wiki/Hooking_up_Pixy_to_a_Microcontroller_(like_an_Arduino)
//
// It prints the detected blocks once per second because printing all of the 
// blocks for all 50 frames per second would overwhelm the Arduino's serial port.
//
   
#include <SPI.h>  
#include <Pixy.h>

// This is the main Pixy object 
Pixy pixy;

void setup()
{
  Serial.begin(9600);

  pixy.init();
}

void loop()
{ 
  static int i = 0;
  int j;
  uint16_t blocks;
  
  // grab blocks!
  blocks = pixy.getBlocks();
  
  // If there are detect blocks, print them!
  if (blocks)
  {
    i++;
    
    // do this (print) every 50 frames because printing every
    // frame would bog down the Arduino
    if (i%20==0)
    {
      ProcessBlocks(pixy.blocks, blocks);
      //Serial.print('/');
//      for (j=0; j<blocks; j++)
//      {
//        PrintBlock(pixy.blocks[j]);
//      }
     //Serial.print('\\');
    }
  }  
}

void ProcessBlocks(Block blocks[], uint16_t blockCount)
{
  if(blockCount >= 2) {
    GetBiggestBlocks(blocks, blockCount);
    Block block1 = blocks[0];
    Block block2 = blocks[1];
    int midpoint = (block1.x + block2.x) / 2;
    int distance = block1.x - block2.x;
    distance = abs(distance);
    PrintMessage(midpoint, distance);
  }
}

int GetArea(Block block)
{
  return block.width * block.height;
}

void GetBiggestBlocks(Block blocks[], uint16_t blockCount)
{
  if(blockCount > 2)
  {
    if(GetArea(blocks[1]) > GetArea(blocks[0]))
    {
      Block temp = blocks[0];
      blocks[0] = blocks[1];
      blocks[1] = temp;
    }
    for(int i = 2; i < blockCount; i++)
    {
      if(GetArea(blocks[i]) > GetArea(blocks[0]))
      {
        blocks[1] = blocks[0];
        blocks[0] = blocks[i];
      }
      else if(GetArea(blocks[i]) > GetArea(blocks[1]))
      {
        blocks[1] = blocks[i];
      }
    }
  }
}

void PrintMessage(int midpoint, int distance)
{
  int idealMidpoint = 160;
  int idealDistance = 150;

  if(midpoint < idealMidpoint - 15)
  {
    Serial.print("1");
  }
  else if(midpoint > idealMidpoint + 15)
  {
    Serial.print("2");
  }
  else if(midpoint < idealMidpoint - 5)
  {
    Serial.print("3");
  }
  else if(midpoint > idealMidpoint + 5)
  {
    Serial.print("4");
  }
  else
  {
    Serial.print("5");
  }
}
