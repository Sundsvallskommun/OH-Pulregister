/*******************************************************************************
 * Copyright (c) 2010 Robert "Unlogic" Olofsson (unlogic@unlogic.se).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 ******************************************************************************/
package se.unlogic.jcaptchautils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaUtils {

	private static final ImageCaptchaService CAPTCHA_SERVICE = new DefaultManageableImageCaptchaService(600, 100000, 75000);

	public static void getCaptchaImage(String id, HttpServletResponse httpServletResponse) {

		try {
			BufferedImage image = CAPTCHA_SERVICE.getImageChallengeForID(id, Locale.ENGLISH);

			httpServletResponse.setHeader("Cache-Control", "no-store");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setDateHeader("Expires", 0);
			httpServletResponse.setContentType("image/jpeg");

			ImageIO.write(image, "jpg", httpServletResponse.getOutputStream());

			httpServletResponse.getOutputStream().flush();
			httpServletResponse.getOutputStream().close();

		} catch (IllegalArgumentException e) {
			return;
		} catch (CaptchaServiceException e) {
			return;
		} catch (SocketException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}

	public static boolean isValidCaptchaCode(String code, String id) {

		try {
			return CAPTCHA_SERVICE.validateResponseForID(id, code);

		} catch (CaptchaServiceException e) {

			return false;
		}
	}
}
