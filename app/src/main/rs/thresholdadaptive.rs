#pragma version(1)
#pragma rs java_package_name(piedel.piotr.thesis.util.imageprocessor)

#define MSG_TAG "BinarizeFromRenderScript"

const static float3 gGrayFactor = { 0.299f, 0.587f, 0.114f };

//Convolution-based parameters
rs_allocation imgIn;
uint32_t kernelSize;
uint32_t width;
uint32_t height;
//Threshold parameters
float thresholdMinValue;
float thresholdMaxValue;
float thresholdValue;


uchar4 __attribute__((kernel))root(const uchar4 in, uint32_t x, uint32_t y) {
       float4 sum = 0;
       uint count = 0;
       uint delta = kernelSize / 2;

        for (int yi = y-delta; yi <= y+delta; ++yi) {
             for (int xi = x-delta; xi <= x+delta; ++xi) {
                if (xi >= 0 && xi < width && yi >= 0 && yi < height) {
                    sum += rsUnpackColor8888(rsGetElementAt_uchar4(imgIn, xi, yi));
                    ++count;
                }
             }
         }

        float4 threshold = sum / count;
        uchar4 value = rsPackColorTo8888(threshold);
        if(in.r > value.r || in.g > value.g || in.b > value.b){
            value = (uchar4)(thresholdMaxValue, thresholdMaxValue, thresholdMaxValue, 255);
        }
        else{
            value = (uchar4)(0, 0, 0, 0);
        }
        return value;
}

void init(){
	rsDebug("Called init adaptive threshold (binarizing but better?) ", rsUptimeMillis());
}