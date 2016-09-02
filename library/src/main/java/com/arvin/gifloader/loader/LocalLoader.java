/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 bboyfeiyu@gmail.com ( Mr.Simple )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.arvin.gifloader.loader;

import android.net.Uri;

import com.arvin.gifloader.request.GifRequest;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author mrsimple
 */
public class LocalLoader extends AbsLoader {

    @Override
    protected byte[] onLoadGif(GifRequest request) {
        byte[] buffer = null;
        final String gifPath = Uri.parse(request.gifUrl).getPath();
        final File gifFile = new File(gifPath);
        if (!gifFile.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(gifFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int n;
            while ((n = fis.read(bytes)) != -1) {
                bos.write(bytes, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
