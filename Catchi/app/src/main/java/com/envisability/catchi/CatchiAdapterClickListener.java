package com.envisability.catchi;

import android.graphics.drawable.Drawable;

import com.envisability.catchi.models.Catchi;

/**
 * Created by Тарас on 08.06.2017.
 */
public interface CatchiAdapterClickListener {
    void onClick(int i, Catchi catchi, Drawable drawable);
}
