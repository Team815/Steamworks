#include <SPI.h>  
#include <Pixy.h>

Pixy pixy;

void setup() {
  Serial.begin(9600);

  pixy.init();
}

void loop() { 
  static int i = 0;
  int j;
  uint16_t blocks;
  
  // grab blocks!
  blocks = pixy.getBlocks();
  
  // If there are detect blocks, print them!
  if (blocks) {
    i++;
    if (i%5==0) {
      ProcessBlocks(pixy.blocks, blocks);
    }
  }  
}

void ProcessBlocks(Block blocks[], uint16_t blockCount) {
  if(blockCount >= 2) {
    GetBiggestBlocks(blocks, blockCount);
    Block blockLeft = blocks[0].x < blocks[1].x ? blocks[0] : blocks[1];
    Block blockRight = blocks[0].x > blocks[1].x ? blocks[0] : blocks[1];
    int midpoint = (blockLeft.x + blockRight.x) / 2;
    int distance = blockRight.x - blockLeft.x;
    PrintMessage(midpoint, distance, blockLeft.width, blockRight.width);
  }
}

int GetArea(Block block) {
  return block.width * block.height;
}

void GetBiggestBlocks(Block blocks[], uint16_t blockCount) {
  if(blockCount > 2) {
    if(GetArea(blocks[1]) > GetArea(blocks[0])) {
      Block temp = blocks[0];
      blocks[0] = blocks[1];
      blocks[1] = temp;
    }
    for(int i = 2; i < blockCount; i++) {
      if(GetArea(blocks[i]) > GetArea(blocks[0])) {
        blocks[1] = blocks[0];
        blocks[0] = blocks[i];
      } else if(GetArea(blocks[i]) > GetArea(blocks[1])) {
        blocks[1] = blocks[i];
      }
    }
  }
}

void PrintMessage(int midpoint, int distance, int widthLeft, int widthRight) {
  int borderWidth = 20;
  int leftEdge = midpoint - distance / 2.0 - widthLeft / 2;
  int rightEdge = 320 - midpoint - distance / 2.0 - widthRight / 2;
  int angle;
  if(leftEdge <= borderWidth) {
    angle = 180;
  } else if(rightEdge <= borderWidth) {
    angle = 0;
  } else {
    int cameraEdge = rightEdge + leftEdge - 20;
    double percent = (leftEdge - 20.0) / (cameraEdge - 20.0);
    angle = 180 - percent * 180;
  }

  if(distance >= 60) {
    if(angle < 85) {
      angle = 0;
    } else if(angle > 95) {
      angle = 180;
    } else {
      Serial.print("<a>");
      return;
    }
  }
  Serial.print('<');
  Serial.print(angle);
  Serial.print('>');
}
