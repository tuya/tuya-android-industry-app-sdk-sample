package com.tuya.iotapp.network.utils;

import android.text.TextUtils;

import com.tuya.dev.json_parser.api.JsonParser;
import com.tuya.iotapp.network.business.BusinessResponse;

import org.json.JSONObject;

import static com.tuya.iotapp.network.business.CommonBusinessError.ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION;

/**
 * ParseHelper 数据解析
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 3:09 PM
 */
public class ParseHelper {
    public static String parser(BusinessResponse bizResponse) {
        if (bizResponse.getResult() == null) {
            return null;
        }
        return bizResponse.getResult().toString();
    }

    /**
     * 解析普通对象数据
     *
     * @param bizResponse
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parser(BusinessResponse bizResponse, Class<T> clazz) {
        return JsonParser.parseObject(parser(bizResponse), clazz);
    }

    /**
     * 解析返回数据
     *
     * @param bizResponse
     * @param clazz
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T parser(BusinessResponse bizResponse, Class<T> clazz, String key) {
        try {
            if (TextUtils.isEmpty(key)) {
                if (clazz == String.class) {
                    return (T) parser(bizResponse);
                }
                return parser(bizResponse, clazz);
            }
            JSONObject result = parser(bizResponse, JSONObject.class);
            if (result != null) {
                return (T) result.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bizResponse.setSuccess(false);
            bizResponse.setCode(Integer.valueOf(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorCode()));
            bizResponse.setMsg(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorMsg());
        }
        return null;
    }

//    /**
//     * 解析列表数据
//     *
//     * @param bizResponse
//     * @param clazz
//     * @param listKey
//     * @param totalKey
//     * @param <T>
//     * @return
//     */
//    public static <T> PageList<T> parse2PageList(BusinessResponse bizResponse, Class<T> clazz, String listKey, String totalKey) {
//        PageList<T> pageList = new PageList<>();
//        if (bizResponse.isSuccess()) {
//            try {
//                int total = 0;
//                String jsonString = parser(bizResponse);
//                if (!TextUtils.isEmpty(jsonString)) {
//                    List<T> lists = new ArrayList<>();
//                    if (TextUtils.isEmpty(listKey)) {
//                        lists = JSON.parseArray(jsonString, clazz);
//                    } else {
//                        JSONObject jsonObject = JSON.parseObject(jsonString);
//                        if (jsonObject != null && jsonObject.size() > 0) {
//                            if (!TextUtils.isEmpty(totalKey)) {
//                                total = jsonObject.getInteger(totalKey);
//                            }
//                            JSONArray jsonArray = jsonObject.getJSONArray(listKey);
//                            if (jsonArray != null) {
//                                lists = JSON.parseArray(jsonArray.toJSONString(), clazz);
//                            }
//                        }
//                    }
//                    int len = lists.size();
//
//                    pageList.setData(lists);
//                    if (listKey == null) {
//                        pageList.setTotal(len);
//                    } else {
//                        if (total >= len) {
//                            pageList.setTotal(total);
//                        } else {
//                            pageList.setTotal(len);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                pageList.setData(new ArrayList<T>());
//                pageList.setTotal(-1);
//                bizResponse.setSuccess(false);
//                bizResponse.setCode(Integer.valueOf(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorCode()));
//                bizResponse.setMsg(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorMsg());
//            }
//        } else {
//            pageList.setData(new ArrayList<T>());
//            pageList.setTotal(-1);
//        }
//
//        return pageList;
//    }
//
//    /**
//     * 解析map数据
//     *
//     * @param bizResponse
//     * @param clazz
//     * @param arrKeys
//     * @param <T>
//     * @return
//     */
//    public static <T> HashMap<String, T> parse2HashMap(BusinessResponse bizResponse, Class<T> clazz, String[] arrKeys) {
//        LinkedHashMap<String, T> hashMap = new LinkedHashMap<>();
//        if (bizResponse.isSuccess()) {
//            try {
//                JSONObject o = parser(bizResponse, JSONObject.class);
//                if (o != null) {
//                    Set<String> keySet;
//                    if (arrKeys == null || arrKeys.length == 0) {
//                        keySet = o.keySet();
//                    } else {
//                        keySet = new HashSet<>(Arrays.asList(arrKeys));
//                    }
//                    for (String key : keySet) {
//                        if (!TextUtils.isEmpty(key)) {
//                            hashMap.put(key, o.getObject(key, clazz));
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                bizResponse.setSuccess(false);
//                bizResponse.setCode(Integer.valueOf(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorCode()));
//                bizResponse.setMsg(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorMsg());
//            }
//        }
//        return hashMap;
//    }
//
//    /**
//     * 解析1维数组
//     *
//     * @param bizResponse
//     * @param clazz
//     * @param listKey
//     * @param <T>
//     * @return
//     */
//    public static <T> ArrayList<T> parse2ArrayList(BusinessResponse bizResponse, Class<T> clazz, String listKey) {
//        ArrayList<T> lists = new ArrayList<>();
//        if (bizResponse.isSuccess()) {
//            try {
//                String jsonString = parser(bizResponse);
//                if (!TextUtils.isEmpty(jsonString)) {
//                    if (TextUtils.isEmpty(listKey)) {
//                        JSONArray objects = JSON.parseArray(jsonString, Feature.OrderedField);
//                        if (objects == null || objects.size() == 0) {
//                            return lists;
//                        }
//                        Object obj = objects.get(0);
//                        if (obj instanceof String
//                                || obj instanceof Integer
//                                || obj instanceof Float
//                                || obj instanceof Double
//                                || obj instanceof Boolean
//                                || obj instanceof Byte
//                                || obj instanceof Short
//                                || obj instanceof Long
//                                || obj instanceof Character) {
//                            lists = (ArrayList<T>) JSON.parseArray(jsonString, clazz);
//                        } else {
//                            for (int i=0,len=objects.size();i<len;i++) {
//                                JSONObject jsonObject = objects.getJSONObject(i);
//                                T t = JSONObject.toJavaObject(jsonObject, clazz);
//                                lists.add(t);
//                            }
//                        }
//                    } else {
//                        JSONObject jsonObject = JSON.parseObject(jsonString);
//                        if (jsonObject != null && jsonObject.size() > 0) {
//                            JSONArray jsonElement = jsonObject.getJSONArray(listKey);
//                            if (jsonElement != null) {
//                                lists = (ArrayList<T>) JSON.parseArray(jsonElement.toJSONString(), clazz);
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                bizResponse.setSuccess(false);
//                bizResponse.setCode(Integer.valueOf(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorCode()));
//                bizResponse.setMsg(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorMsg());
//            }
//        }
//
//        return lists;
//    }
//
//    /**
//     * 解析2维数组
//     *
//     * @param bizResponse
//     * @param clazz
//     * @param listKey
//     * @param <T>
//     * @return
//     */
//    public static <T> ArrayList<ArrayList<T>> parse2ArrayLists(BusinessResponse bizResponse, Class<T> clazz, String listKey) {
//        ArrayList<ArrayList<T>> arrayLists = new ArrayList<>();
//
//        if (bizResponse.isSuccess()) {
//            try {
//                String jsonString = parser(bizResponse);
//                if (!TextUtils.isEmpty(jsonString)) {
//                    if (!TextUtils.isEmpty(listKey)) {
//                        JSONArray jsonArray = null;
//                        JSONObject jsonObject = JSON.parseObject(jsonString);
//                        if (jsonObject != null) {
//                            jsonArray = jsonObject.getJSONArray(listKey);
//                        }
//                        if (jsonArray == null) {
//                            jsonArray = new JSONArray();
//                        }
//                        jsonString = jsonArray.toJSONString();
//                    }
//
//                    List<String> jsonLists = JSON.parseArray(jsonString, String.class);
//                    if (jsonLists != null) {
//                        for (String string : jsonLists) {
//                            ArrayList<T> lists = (ArrayList<T>) JSON.parseArray(string, clazz);
//                            arrayLists.add(lists);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                bizResponse.setSuccess(false);
//                bizResponse.setCode(Integer.valueOf(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorCode()));
//                bizResponse.setMsg(ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION.getErrorMsg());
//            }
//        }
//
//        return arrayLists;
//    }
}
