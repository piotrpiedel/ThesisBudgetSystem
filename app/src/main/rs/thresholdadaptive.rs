#pragma version(1)
#pragma rs java_package_name(piedel.piotr.thesis.util.imageprocessor)

#define MSG_TAG "BinarizeFromRenderScript"

const float thresholdMaxValue = 255.0;
void root(const uchar4 *v_in, uchar4 *v_out) {
	 float4 sum = 0;
     uint count = 0;
     uint delta = 16 / 2;

     for (int yi = y-delta; yi <= y+delta; ++yi) {
         for (int xi = x-delta; xi <= x+delta; ++xi) {
            if (xi >= 0 && xi < width && yi >= 0 && yi < height) {
                    sum += rsUnpackColor8888(rsGetElementAt_uchar4(imgIn, xi, yi));
                ++count;
            }
          }
      }

     float4 threshold = sum / count;
     char4 val = rsPackColorTo8888(threshold);

     if(v_in.r > val.r || v_in.g > val.g || v_in.b > val.b){
         val = (uchar4)(thresholdMaxValue, thresholdMaxValue, thresholdMaxValue, 255);
     }
        else{
            val = (uchar4)(0, 0, 0, 0);
        }

        return val;
}

void init(){
	rsDebug("Called init adaptive threshold (binarizing but better?) ", rsUptimeMillis());
}