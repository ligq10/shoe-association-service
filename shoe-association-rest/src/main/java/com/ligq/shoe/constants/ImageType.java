package com.ligq.shoe.constants;

public enum ImageType {
	PNG("png"),
	JPEG("jpeg"),
	GIF("gif"),
	JPG("jpg"),
	BMP("bmp");
	

	private final String mimetype;

	private ImageType( String mimetype) {
		this.mimetype = mimetype;
	}
	
	/**
	 * Return the mimetype of this image.
	 */
	public String getMimetype() {
		return mimetype;
	}
	
}
