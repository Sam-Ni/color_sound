package com.example.colorsound.util

import android.util.Log

interface IPoolable {
    val onInit: () -> Unit
    val onGet: () -> Unit
    val onRelease: () -> Unit
}

private data class ObjectPair<T>(var isUsed: Boolean, var obj: T) where T : IPoolable

class ObjectPool<T>(size: Int, val initT: () -> T) where T : IPoolable {
    private val objects: List<ObjectPair<T>> = List(size = size, init = {
        val objectPair = ObjectPair(false, initT())
        objectPair.obj.onInit()
        objectPair
    })

    fun get(): T {
        val find = objects.find { !it.isUsed }
        return if (find != null) {
            find.isUsed = true
            find.obj.onGet()
            find.obj
        } else {
            val pair = ObjectPair(true, initT())
            pair.obj.onInit()
            objects.plus(pair)
            pair.obj.onGet()
            pair.obj
        }
    }

    fun release(obj: T?) {
        if (obj == null) {
            Log.w("op", "obj 是 null")
            return
        }
        val find = objects.find { it.obj == obj }
        if (find != null) {
            if (find.isUsed) {
                find.isUsed = false
                find.obj.onRelease()
            } else {
                Log.w("op", "释放了本来就没有使用的obj：" + find.obj.toString())
            }
        } else {
            Log.w("op", "在pool中找不到obj： $obj")
        }
    }
}