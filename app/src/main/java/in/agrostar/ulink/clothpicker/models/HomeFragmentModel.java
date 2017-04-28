package in.agrostar.ulink.clothpicker.models;

import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.domain.SuggestionPool;
import in.agrostar.ulink.clothpicker.domain.UploadMap;
import in.agrostar.ulink.clothpicker.domain.UploadObject;
import in.agrostar.ulink.clothpicker.domain.UploadType;
import in.agrostar.ulink.clothpicker.utils.GsonUtils;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;
import in.agrostar.ulink.clothpicker.utils.TransferUtil;

/**
 * Created by ayush on 21/4/17.
 */

public class HomeFragmentModel extends BaseModel {

    private final TransferUtility transferUtility;
    private final SharedPreferencesUtil preferences;
    WeakReference<Context> contextWeakReference;
    private UploadMap trouserUplaodMap;
    private UploadMap shirtUplaodMap;
    private SuggestionPool suggestionPool;

    public HomeFragmentModel(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
        transferUtility = TransferUtil.getTransferUtility(context);
        preferences = SharedPreferencesUtil.getInstance(context);
    }

    public TransferObserver uploadFile(File file) {

        //can use observer but right now here now doing anything with it.
        TransferObserver observer = transferUtility.upload(Constants.VALUES.BUCKET_NAME, file.getName(),
                file);
        return observer;
    }

    public void pushFileToBackend(){
        //save file at the backend
    }

    public List<TransferObserver> getUploads() {
        return transferUtility.getTransfersWithType(TransferType.UPLOAD);
    }

    public void delete(int id) {
        transferUtility.deleteTransferRecord(id);
    }

    public TransferObserver resume(int id) {
        return transferUtility.resume(id);
    }

    public Void getUploadMap() {

        String shirtUploadMapString = preferences.getData(Constants.KEY.UPLOAD_MAP_SHIRT, null);
        if (shirtUploadMapString != null) {
            shirtUplaodMap = GsonUtils.getObjectFromJson(shirtUploadMapString, UploadMap.class);
        } else {
            shirtUplaodMap = new UploadMap();
            shirtUplaodMap.setUploadMap( new HashMap<Integer, UploadObject>());
        }

        String trouserUploadMapString = preferences.getData(Constants.KEY.UPLOAD_MAP_TROUSER, null);
        if (trouserUploadMapString != null) {
            trouserUplaodMap = GsonUtils.getObjectFromJson(trouserUploadMapString, UploadMap.class);
        } else {
            trouserUplaodMap = new UploadMap();
            trouserUplaodMap.setUploadMap( new HashMap<Integer, UploadObject>());
        }


        String suggestionPoolString = preferences.getData(Constants.KEY.SUGGESTION_POOL, null);
        if (suggestionPoolString != null) {
             suggestionPool= GsonUtils.getObjectFromJson(suggestionPoolString, SuggestionPool.class);
        } else {
            List<Suggestion1> suggestions = new ArrayList<>();
            suggestionPool = new SuggestionPool();
            suggestionPool.setSuggestionList(suggestions);

        }
        return null;

    }

    public void addUpload(TransferObserver observer, UploadType type) {
        if (type.toString().equalsIgnoreCase(UploadType.SHIRT.toString()) && shirtUplaodMap != null) {
            UploadObject tempUploadObject = new UploadObject();
            tempUploadObject.setTransferId(observer.getId());
            tempUploadObject.setFilePath(observer.getAbsoluteFilePath());
            tempUploadObject.setFileUrl(observer.getKey());
            tempUploadObject.setState(observer.getState());
            tempUploadObject.setType(type);
            shirtUplaodMap.getUploadMap().put(observer.getId(), tempUploadObject);
        } else  if (type.toString().equalsIgnoreCase(UploadType.TROUSER.toString()) && trouserUplaodMap != null) {
            UploadObject tempUploadObject = new UploadObject();
            tempUploadObject.setTransferId(observer.getId());
            tempUploadObject.setFilePath(observer.getAbsoluteFilePath());
            tempUploadObject.setFileUrl(observer.getKey());
            tempUploadObject.setState(observer.getState());
            tempUploadObject.setType(type);
            trouserUplaodMap.getUploadMap().put(observer.getId(), tempUploadObject);
        }
    }

    public void updateUpload(int id) {
        UploadObject tempUploadObject;
        if (shirtUplaodMap != null && shirtUplaodMap.getUploadMap().containsKey(id)) {
            tempUploadObject = shirtUplaodMap.getUploadMap().get(id);
            tempUploadObject.setState(TransferState.COMPLETED);
            String temp = GsonUtils.getJsonString(shirtUplaodMap);
            preferences.saveData(Constants.KEY.UPLOAD_MAP_SHIRT,temp);
           addNewShirtObjectToSuggestionPool(tempUploadObject);
        } else if (trouserUplaodMap != null && trouserUplaodMap.getUploadMap().containsKey(id)) {
            tempUploadObject = trouserUplaodMap.getUploadMap().get(id);
            tempUploadObject.setState(TransferState.COMPLETED);
            String temp = GsonUtils.getJsonString(trouserUplaodMap);
            preferences.saveData(Constants.KEY.UPLOAD_MAP_TROUSER,temp);
            addNewTrouserToSuggestionPool(tempUploadObject);
        }
    }

    private void addNewTrouserToSuggestionPool(UploadObject tempUplaodObject) {
        int count = suggestionPool.getSuggestionList().size();
        if (shirtUplaodMap != null && !shirtUplaodMap.getUploadMap().isEmpty()) {
            for(UploadObject uploadObject: shirtUplaodMap.getUploadMap().values()) {
                Suggestion1 suggestion = new Suggestion1();
                List<UploadObject> tempUploadObjects = new ArrayList<>();
                tempUploadObjects.add(tempUplaodObject);
                tempUploadObjects.add(uploadObject);
                suggestion.setUploadObjects(tempUploadObjects);
                suggestion.setFlag(false);
                suggestion.setAlreadySeen(false);
                suggestion.setId(count++);
                suggestionPool.getSuggestionList().add(suggestion);
            }
            String tempString = GsonUtils.getJsonString(suggestionPool);
            preferences.saveData(Constants.KEY.SUGGESTION_POOL,tempString);
        }
    }

    private void addNewShirtObjectToSuggestionPool(UploadObject tempUplaodObject) {
        int count = suggestionPool.getSuggestionList().size();
        if (trouserUplaodMap != null && !trouserUplaodMap.getUploadMap().isEmpty()) {
            for(UploadObject uploadObject: trouserUplaodMap.getUploadMap().values()) {
                Suggestion1 suggestion = new Suggestion1();
                List<UploadObject> tempUploadObjects = new ArrayList<>();
                tempUploadObjects.add(tempUplaodObject);
                tempUploadObjects.add(uploadObject);
                suggestion.setUploadObjects(tempUploadObjects);
                suggestion.setFlag(false);
                suggestion.setAlreadySeen(false);
                suggestion.setId(count++);
                suggestionPool.getSuggestionList().add(suggestion);
            }
            String tempString = GsonUtils.getJsonString(suggestionPool);
            preferences.saveData(Constants.KEY.SUGGESTION_POOL,tempString);
        }
    }
}
