package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.nfd.NFDFilterItem;
import org.lwjgl.util.nfd.NFDPathSetEnum;
import org.lwjgl.util.nfd.NativeFileDialog;

import java.io.File;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class FileDialogs {
    public static Array<FileHandle> openMultipleDialog(String defaultPath, String[] filterPatterns, String[] filterDescriptions) {
        //fix file path characters
        if (isWindows()) {
            defaultPath = defaultPath.replace("/", "\\");
        } else {
            defaultPath = defaultPath.replace("\\", "/");
        }

        try (MemoryStack stack = stackPush()) {
            var filters = NFDFilterItem.malloc(filterPatterns.length);
            for (int i = 0; i < filterPatterns.length; i++) {
                filters.get(i)
                    .name(stack.UTF8(filterDescriptions[i]))
                    .spec(stack.UTF8(filterPatterns[i]));
            }

            var pp = stack.mallocPointer(1);

            var status = NativeFileDialog.NFD_OpenDialogMultiple(pp, filters, stack.UTF8(defaultPath));

            if (status == NFD_CANCEL) return null;
            if (status != NFD_OKAY) System.err.format("Error: %s\n", NFD_GetError());

            long pathSet = pp.get(0);
            var psEnum = NFDPathSetEnum.calloc(stack);
            NFD_PathSet_GetEnum(pathSet, psEnum);

            var paths = new Array<FileHandle>();
            while (NFD_PathSet_EnumNext(psEnum, pp) == NFD_OKAY && pp.get(0) != NULL) {
                var path = pp.getStringUTF8(0);
                paths.add(Gdx.files.absolute(path));
                NFD_PathSet_FreePath(pp.get(0));
            }

            NFD_PathSet_FreeEnum(psEnum);
            NFD_PathSet_Free(pathSet);
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FileHandle openDialog(String defaultPath, String[] filterPatterns, String[] filterDescriptions) {
        //fix file path characters
        if (isWindows()) {
            defaultPath = defaultPath.replace("/", "\\");
        } else {
            defaultPath = defaultPath.replace("\\", "/");
        }

        try (MemoryStack stack = stackPush()) {
            var filters = NFDFilterItem.malloc(filterPatterns.length);
            for (int i = 0; i < filterPatterns.length; i++) {
                filters.get(i)
                    .name(stack.UTF8(filterDescriptions[i]))
                    .spec(stack.UTF8(filterPatterns[i]));
            }

            PointerBuffer pp = stack.mallocPointer(1);
            var status = NFD_OpenDialog(pp, filters, stack.UTF8(defaultPath));

            if (status == NativeFileDialog.NFD_CANCEL) return null;
            else if (status == NFD_OKAY) {
                var file = Gdx.files.absolute(pp.getStringUTF8(0));
                NFD_FreePath(pp.get(0));
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static FileHandle saveDialog(String defaultPath, String defaultName, String[] filterPatterns, String[] filterDescriptions) {
        //fix file path characters
        if (isWindows()) {
            defaultPath = defaultPath.replace("/", "\\");
        } else {
            defaultPath = defaultPath.replace("\\", "/");
        }

        try (MemoryStack stack = stackPush()) {
            var filters = NFDFilterItem.malloc(filterPatterns.length);
            for (int i = 0; i < filterPatterns.length; i++) {
                filters.get(i)
                    .name(stack.UTF8(filterDescriptions[i]))
                    .spec(stack.UTF8(filterPatterns[i]));
            }

            PointerBuffer pp = stack.mallocPointer(1);
            var status = NFD_SaveDialog(pp, filters, defaultPath, defaultName);

            if (status == NativeFileDialog.NFD_CANCEL) return null;
            else if (status == NFD_OKAY) {
                var file = Gdx.files.absolute(pp.getStringUTF8(0));
                NFD_FreePath(pp.get(0));
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String os;

    public static boolean isWindows() {
        if (os == null) {
            os = System.getProperty("os.name");
        }

        return os.startsWith("Windows");
    }

    public static boolean isLinux() {
        if (os == null) {
            os = System.getProperty("os.name");
        }
        return os.startsWith("Linux");
    }

    public static boolean isMac() {
        if (os == null) {
            os = System.getProperty("os.name");
        }
        return os.startsWith("Mac");
    }
}
