package com.example.appgrouppurchasemaching.board

import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * 첨부파일(이미지)의 크기와 관련된 Utility class
 */
class AttachmentSizeUtil {
    companion object {
        val maxWidth: Int = 1500
        val maxHeight: Int = 1500

        /**
         * BitmapFactory 를 사용할 때, 이미지 축소 버전을 메모리로 로드 할 수 있게 도와주는 함수
         *
         * @param[fileUri] Resource URI
         * @param[reqWidth] Request maximum width
         * @param[reqHeight] Request maximum height
         *
         * @see[https://developer.android.com/topic/performance/graphics/load-bitmap?hl=ko#load-bitmap]
         */
        fun decodeSampledBitmapFromResource(fileUri: String, reqWidth: Int, reqHeight: Int): Bitmap {
            // First decode with inJustDecodeBounds=true to check dimensions
            return BitmapFactory.Options().run {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(fileUri, this)

                // Calculate inSampleSize
                inSampleSize = calculateSampleSize(this, reqWidth, reqHeight)

                // Decode bitmap with inSampleSize set
                inJustDecodeBounds = false
                BitmapFactory.decodeFile(fileUri, this)
            }
        }

        /**
         * Input 이미지 크기를 줄이기 위해 필요한 [sampleSize] 를 계산해주는 함수
         *
         * 이미지의 [width]와 [height]의 비율을 유지하면서 [reqWidth]와 [reqHeight]를 넘지 않도록 샘플링 하는 [sampleSize]를 계산한다.
         *
         * [decodeSampledBitmapFromResource] 함수에서만 사용
         *
         * @param[options] 현재 사용중인 BitmapFactory.Options instance
         * @param[reqWidth] Request maximum width
         * @param[reqHeight] Request maximum height
         */
        fun calculateSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            return calculateSampleSize(height, width, reqWidth, reqHeight)
        }

        /**
         * Input 이미지 크기를 줄이기 위해 필요한 [sampleSize] 를 계산해주는 함수
         *
         * 이미지의 [width]와 [height]의 비율을 유지하면서 [reqWidth]와 [reqHeight]를 넘지 않도록 샘플링 하는 [sampleSize]를 계산한다.
         *
         * @param[width] Raw width
         * @param[height] Raw height
         * @param[reqWidth] Request maximum width
         * @param[reqHeight] Request maximum height
         */
        fun calculateSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
            var inSampleSize = 1

            // 크기 제한 조건이 충족되지 않으면 샘플링 크기를 2배씩 늘린다.
            while (!((width / inSampleSize <= reqWidth) && (height / inSampleSize <= reqHeight))) {
                inSampleSize *= 2
            }

            return inSampleSize
        }
    }
}