package com.l2f.vitheakids.model;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class ExerciseTypeRecord {
	
	private String exerciseTypeName;
	private int exerciseTypeImage;

	public ExerciseTypeRecord() {}
	public ExerciseTypeRecord(String exerciseTypeName, int exerciseTypeImage) {
		super();
		this.exerciseTypeName = exerciseTypeName;
		this.exerciseTypeImage = exerciseTypeImage;
	}

	/**
	 * @return the exerciseTypeName
	 */
	public String getExerciseTypeName() {
		return exerciseTypeName;
	}
    /**
     * @return the exerciseTypeImage
     */
    public int getExerciseTypeImage() {
        return exerciseTypeImage;
    }


	/**
	 * @param exerciseTypeName the exerciseTypeName to set
	 */
	public void setExerciseTypeName(String exerciseTypeName) {
		this.exerciseTypeName = exerciseTypeName;
	}
	/**
	 * @param exerciseTypeImage the exerciseTypeImage to set
	 */
	public void setExerciseTypeImage(int exerciseTypeImage) {
		this.exerciseTypeImage = exerciseTypeImage;
	}

}
