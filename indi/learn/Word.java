/**
 * @version 2020-06-08
 * @author puntsok
 * 
 *  单词分析类
 */
//package analysis;
package indi.learn;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class Word {
    // 用于标识result的index值
    // 分别为 前加字、上加字、基字、下加字、音标、后加字、重后加字
    private final int PREFIX = 0;
    private final int SUPER_SCRIPT = 1;
    private final int BASE = 2;
    private final int UNDER_SCRIPT = 3;
    private final int SYMBOL = 4;
    private final int BEHIND = 5;
    private final int REPEAT_BEHIND = 6;
    
    // ************ 只读数据
    private final String[] words = new String[]{
        "ཀ", "ཁ", "ག", "ང", "ཅ", "ཆ", "ཇ", "ཉ", 
        "ཏ", "ཐ", "ད", "ན", "པ", "ཕ", "བ", "མ", 
        "ཙ", "ཚ", "ཛ", "ཝ", "ཞ", "ཟ", "འ", "ཡ",
        "ར", "ལ", "ཤ", "ས", "ཧ", "ཨ"};
    private final String[] prefix = new String[]{"ག", "ད", "བ", "མ", "འ"};
    private final String[] superScript = new String[]{"ར", "ལ", "ས", "ག", "ད", "ཛ", "བ", "ཧ"};
    private final String[] underScript = new String[]{"ྱ", "ྲ", "ླ", "ྭ"};
    private final String[] behind = new String[]{"ག", "ང", "ད", "ན", "བ", "མ", "འ", "ར", "ལ", "ས"};
    private final String[] repeatBehind = new String[]{"ད", "ས"}; //ད 虽然也是重后加字，但是现代藏文里已弃用
    private final String[] symbol = new String[]{"ི",  "ུ", "ེ", "ོ",  "ཽ",  "ྀ",  "ཻ"};
    private final Map<String, String> subWordMap = new HashMap<String, String>(){
        private static final long serialVersionUID = 1L; //序列化ID
        {
        put("ཀ", "ྐ"); put("ཁ", "ྑ"); put("ག", "ྒ"); put("ང", "ྔ"); 
        put("ཅ", "ྕ"); put("ཆ", "ྖ"); put("ཇ", "ྗ"); put("ཉ", "ྙ");
        put("ཏ", "ྟ"); put("ཐ", "ྠ"); put("ད", "ྡ"); put("ན", "ྣ"); 
        put("པ", "ྤ"); put("ཕ", "ྥ"); put("བ", "ྦ"); put("མ", "ྨ");
        put("ཙ", "ྩ"); put("ཚ", "ྪ"); put("ཛ", "ྫ"); put("ཝ", "ྺ");
        put("ཞ", "ྮ"); put("ཟ", "ྯ"); put("འ", "ྰ"); put("ཡ", "ྱ"); 
        put("ར", "ྲ"); put("ལ", "ླ"); put("ཤ", "ྴ"); put("ས", "ྶ");
        put("ཧ", "ྷ"); put("ཨ", "ྸ");
        }
    };
    private final Map<String, String> subWordMapReversed = reverse(subWordMap); //键值互换
    private final String[] subWord = new String[]{
        "ྐ", "ྑ", "ྒ", "ྔ", "ྕ", "ྖ", "ྗ", "ྙ", 
        "ྟ", "ྠ", "ྡ", "ྣ", "ྤ", "ྥ", "ྦ", "ྨ", 
        "ྩ", "ྪ", "ྫ", "ྺ", "ྮ", "ྯ", "ྰ", "ྱ", 
        "ྲ", "ླ", "ྴ", "ྶ", "ྷ", "ྸ"};
    private final String[] specialWords = new String[]{"ཛྙ", "ཧྥ", "གྷ",  "དྷ",  "ཛྷ", "བྷ"};
    private final String[] specialWordsUnder = new String[]{"ྙ", "ྥ", "ྷ"};
    // 上加字 正字规范
    private final String[] superRa = new String[]{"ཀ", "ག", "ང", "ཇ", "ཉ", "ཏ", "ད", "ན", "བ", "མ", "ཙ", "ཛ"};
    private final String[] superLa = new String[]{"ཀ", "ག", "ང", "ཇ", "ཅ", "ཏ", "ད", "པ", "བ", "ཧ"};
    private final String[] superSa = new String[]{"ཀ", "ག", "ང", "ཉ", "ཏ", "ད", "ན", "པ", "བ", "མ", "ཙ"};

    // 下加字正字规范
    private final String[] underYa = new String[]{"ཀ", "ཁ", "ག", "པ", "ཕ", "པ", "མ"};
    private final String[] underRa = new String[]{"ཀ", "ཁ", "ག", "ཏ", "ད", "པ", "ཕ", "བ", "མ", "ཤ", "ས", "ཧ"};
    private final String[] underLa = new String[]{"ཀ", "ག", "བ", "ཟ", "ར", "ས"};
    private final String[] underWa = new String[]{"ཀ", "ཁ", "ག", "ཉ", "ད", "ཙ", "ཚ", "ཞ", "ཟ", "ར", "ལ", "ཤ", "ས", "ཧ"};

    private String input;       // 对该字符进行分析
    private String[] result;    // 分析结果池
    //构造器
    Word(String inputString) {
        // 初始化全局结果写入池
        result = new String[] {null, null, null, null, null, null, null};
        // 对输入字符做初步的判断
        if (inputString.length() == 0) {
            System.out.print(">>> 输入字符为空！");
        } else if (inputString.substring(inputString.length() - 1).equals("་") 
            || inputString.substring(inputString.length() - 1).equals("།")) {
            if (inputString.length() <= 8) {
                this.input = inputString.substring(0, inputString.length() - 1);
            } else {
                System.out.print(">>> 输入字符过长，为非法字符！");
            }
        } else {
            if (inputString.length() <= 7) {
                this.input = inputString;
            } else {
                System.out.print(">>>输入字符过长！");
            }
        }
    }

    // **************公开方法
    public String getPrefix() {
        work();
        return result[PREFIX];
    }

    public String getSuperScript() {
        work();
        return result[SUPER_SCRIPT];
    }

    public String getBase() {
        work();
        return result[BASE];
    }

    public String getUnderScript() {
        work();
        return result[UNDER_SCRIPT];
    }

    public String getSymbol() {
        work();
        return result[SYMBOL];
    }

    public String getBehind() {
        work();
        return result[BEHIND];
    }

    public String getRepeatBhind() {
        work();
        return result[REPEAT_BEHIND];
    }

    public String[] getAll() {
        work();
        return result;
    }

    public boolean getCorrectness() {
        return true;
    }

    /** 供公开方法使用 -- 工作中心/调度中心 */
    private void work() {
        boolean[] functions = new boolean[]{
            this.ifExistSingle(),
            };
        for (boolean func: functions){
            if (func) 
                break;       
        } 
    }

    /** 字符串数组交操作 
     * @param arr1 String[] 待处理字符
     * @param arr2 String[] 待处理字符
     * @return String[] 返回交集
    */
    private String[] intersect(String[] arr1, String[] arr2) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        LinkedList<String> list = new LinkedList<String>();
        for (String str : arr1) {
            if (!map.containsKey(str)) {
                map.put(str, Boolean.FALSE);
            }
        }
        for (String str : arr2) {
            if (map.containsKey(str)) {
                map.put(str, Boolean.TRUE);
            }
        }
        for (Entry<String, Boolean> e : map.entrySet()) {
            if (e.getValue().equals(Boolean.TRUE)) {
                list.add(e.getKey());
            }
        }
        String[] result = {};
        return list.toArray(result);
    }

    /** 字符串数组交操作 (重载)
     * @param arr1 char[] 待处理字符
     * @param arr2 String[] 待处理字符
     * @return String[] 返回交集
     *
    */
    private String[] intersect(char[] arr1, String[] arr2) {
        // arr1 : char[] --> String[]
        int index = 0;
        String[] arr1Str = new String[arr1.length+1];
        for(char arr: arr1){
            arr1Str[index] =  String.valueOf(arr);
            index += 1;
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        LinkedList<String> list = new LinkedList<String>();
        for (String str : arr1Str) {
            if (!map.containsKey(str)) {
                map.put(str, Boolean.FALSE);
            }
        }
        for (String str : arr2) {
            if (map.containsKey(str)) {
                map.put(str, Boolean.TRUE);
            }
        }
        for (Entry<String, Boolean> e : map.entrySet()) {
            if (e.getValue().equals(Boolean.TRUE)) {
                list.add(e.getKey());
            }
        }
        String[] result = {};
        return list.toArray(result);
    }

    // *************** 查找基字的位置、并配合find(),确定字的组成情况
    /** 供 if* 方法调用（只要其中之一能找到基字，本方法就能配合找到剩下组成部分的位置）
     * @param index 基字的索引值
     */
    private void find(int index) {
        // 若字符长度为一时，其必然是基字
        if (input.length() == 1) {
            //char需要转换成String，由于我们需要预存储None信息，所以必须用string
            result[BASE] = String.valueOf(input.charAt(index));  
        }
        // 若基字前只有一个字时
        else if (index == 1) {
            if (Arrays.asList(subWord).contains(String.valueOf(input.charAt(index))))  // 如果基字 为下加字的形态，则前项字符为 上加字
                result[SUPER_SCRIPT] = String.valueOf(input.charAt(index-1));
            else //如果基字为正常形态, 则前项字符为 前加字
                result[PREFIX] = String.valueOf(input.charAt(index-1));
        }
        // 若基字前有两个字  则必然是 前加字 + 上加字
        else if (index == 2) {
            result[SUPER_SCRIPT] = String.valueOf(input.charAt(index-1));
            result[PREFIX] = String.valueOf(input.charAt(index-2));
        }
        // 若基字之后有一个字符
        else if ((input.length()-index) == 2) {
            // 可能是 下加字
            if (Arrays.asList(subWord).contains(String.valueOf(input.charAt(index+1)))
                || Arrays.asList(underScript).contains(String.valueOf(input.charAt(index+1)))){
                result[UNDER_SCRIPT] = String.valueOf(input.charAt(index+1));
            } 
            // 可能是音标
            else if (Arrays.asList(symbol).contains(String.valueOf(input.charAt(index+1)))) {
                result[SYMBOL] = String.valueOf(input.charAt(index+1));
            }
            // 可能是 后加字
            else if (Arrays.asList(words).contains(String.valueOf(input.charAt(index+1)))){
                result[BEHIND] = String.valueOf(input.charAt(index+1));
            }
            // 可能是缩写字
            else if (String.valueOf(input.charAt(index+1)).equals("ཌ")) {
                result[BEHIND] = "ག";
                result[REPEAT_BEHIND] = "ས";
            }        
        }
        // 若基字后 有两个字符
        else if((input.length()-index) == 3){
            //如果第一个是 下加字
            if (Arrays.asList(subWord).contains(String.valueOf(input.charAt(index+1)))
                || Arrays.asList(underScript).contains(String.valueOf(input.charAt(index+1)))) {
                result[UNDER_SCRIPT] = String.valueOf(input.charAt(index+1));
                // 下加字 + 音标的 情况
                if (Arrays.asList(symbol).contains(String.valueOf(input.charAt(index+2)))) {
                    result[SYMBOL] = String.valueOf(input.charAt(index+2));
                }
                // 下加字 + 后加字的情形
                else if (Arrays.asList(words).contains(String.valueOf(input.charAt(index+2)))) {
                    result[BEHIND] = String.valueOf(input.charAt(index+2));
                }
                // 下加字 + 缩写字 的情形
                else if (String.valueOf(input.charAt(index+2)).equals("ཌ")) {
                    result[BEHIND] = "ག";
                    result[REPEAT_BEHIND] = "ས";
                }
            }
            // 如果第一个是 音标
            if (Arrays.asList(symbol).contains(String.valueOf(input.charAt(index+1)))) {
                result[SYMBOL] = String.valueOf(input.charAt(index+1));
                // 音标 + 特殊字 的情形
                if (String.valueOf(input.charAt(index+2)).equals("ཌ")) {
                    result[BEHIND] = "ག";
                    result[REPEAT_BEHIND] = "ས";
                // 音标 + 后加字 的情形
                }else{ 
                    result[BEHIND] = String.valueOf(input.charAt(index+2));
                }
            }
            // 如果第一个是 后加字、 则第二个必然是重后加字
            if (Arrays.asList(words).contains(String.valueOf(input.charAt(index+1)))) {
                result[BEHIND] = String.valueOf(input.charAt(index+1));
                result[REPEAT_BEHIND] = String.valueOf(input.charAt(index+2));
            }
        }
        // 若基字后有三个字符
        else if ((input.length()-index) == 4) {
            //第一个是下加字的情形
            if (Arrays.asList(subWord).contains(String.valueOf(input.charAt(index+1)))
                || Arrays.asList(underScript).contains(String.valueOf(input.charAt(index+1)))) {
                result[UNDER_SCRIPT] = String.valueOf(input.charAt(index+1));
                // 下加字 + 音标 + ××  的情形
                if (Arrays.asList(subWord).contains(String.valueOf(input.charAt(index+2)))) {
                    result[SYMBOL] = String.valueOf(input.charAt(index+2));
                    // 音标后接的是缩写字
                    if (String.valueOf(input.charAt(index+3)) == "ཌ") {
                        result[BEHIND] = "ག";
                        result[REPEAT_BEHIND] = "ས";
                    }else{ // 音标后的就是 后接字了
                        result[BEHIND] = String.valueOf(input.charAt(index+3));
                    }
                // 下加字 + 后加字 + 重后加字的情形
                }else if (Arrays.asList(words).contains(String.valueOf(input.charAt(index+2)))) {
                    result[BEHIND] = String.valueOf(input.charAt(index+2));
                    result[REPEAT_BEHIND] = String.valueOf(input.charAt(index+3));
                }
            }
            // 第一个是音标的情况 ：音标 + 后加字 + 重后加字
            if (Arrays.asList(symbol).contains(String.valueOf(input.charAt(index+1)))) {
                result[SYMBOL] = String.valueOf(input.charAt(index+1));
                result[BEHIND] = String.valueOf(input.charAt(index+2));
                result[REPEAT_BEHIND] = String.valueOf(input.charAt(index+3));
            }
        }
        // 若基字后有四个字符: 下加字 + 音标 + 后加字 + 重后加字
        else if ((input.length()-index) == 4) {
            result[UNDER_SCRIPT] = String.valueOf(input.charAt(index+1));
            result[SYMBOL] = String.valueOf(input.charAt(index+2));
            result[BEHIND] = String.valueOf(input.charAt(index+3));
            result[REPEAT_BEHIND] = String.valueOf(input.charAt(index+4));
        }
    }

    /** མིང་གཞི་རྐྱང་པ་  单个字符成字的情况 
     * @return boolean 找到为true、反之则false
    */
    private boolean ifExistSingle() {
        if (input.length() == 1) {
            result[BASE] = String.valueOf(input.charAt(0)); //存储基字
            return true; // 找到了
        }else{
            return false; // 没找到
        }
    }

    /**
     * མགོ་ཅན་ཡོད་པ་  当存在上加字  寻找基字及其索引值
     * @return boolean 找到为true、反之则false
     */
    private boolean ifExistSuperScript() {
        char[] charInput = input.toCharArray();//获取字符串每个字符
        String[] underWord = intersect(charInput, subWord); //相交、得到下加形态的字符串数组
        // 若存在下加字形态的字符,则可能存在上加字
        if (underWord.length > 0) {
            int indexOfUnderWord = input.indexOf(underWord[0]); //第一个下加字形态的索引值即为基字
            //如果发现 下加字 是 特殊字 的下加字部分、就要针对特殊字的可能情况做特殊处理
            if (Arrays.asList(new String[]{"ྙ", "ྥ", "ྷ"}).contains(underWord[0])){
                // 看看 下加字 上面的字符是不是 也是特殊字的组成部分 - 若是就特殊处理
                if (Arrays.asList(new String[]{"ག", "ད", "ཛ", "བ", "ཧ"}).contains(
                    String.valueOf(input.charAt(indexOfUnderWord-1)))) { 
                    // 此处不对 特殊字 做正字规范的判断                    
                    int index = indexOfUnderWord; // 找到了基字索引
                    // 基字我认为存储成整个 特殊字 效果会好，有待考证
                    result[BASE] = (String.valueOf(input.charAt(index-1)) + String.valueOf(input.charAt(index)));
                    // 沿着这个线索 寻找剩余的其他元素
                    find(index);
                    // 特殊字上下无加字情况，上下加字位置可以再次确认置空
                    result[SUPER_SCRIPT] = null;
                    result[UNDER_SCRIPT] = null;
                    return true;
                }// 虽然下加字是 特殊字 的组成部分 但字并不是特殊字的情况 
                else{ // 比如 རྙ（ཉ虽然是特殊字的组成部分，但是这里是基字）等情况    
                    int index = indexOfUnderWord;
                    // 基字 不能存储下字字形态的字符，需要以正常形态存储
                    result[BASE] = subWordMapReversed.get(String.valueOf(input.charAt(index)));
                    find(index); // 寻找其他元素
                    return true; 
                }
            }
            // 如果无特殊字干扰即为普遍情况
            else{
                int index = indexOfUnderWord;
                // 基字 不能存储下字字形态的字符，需要以正常形态存储
                result[BASE] = subWordMapReversed.get(String.valueOf(input.charAt(index)));
                // 寻找其他元素
                find(index);
                return true;
            }
        }
        // 若 无 下加形态的字符、则不可能存在上加字, 即元素位置在该方法内找不到了
        else{ 
            return false;
        }    
    }

    /** འདོགས་ཅན་ཡོད་པ་  当存在下加字  寻找基字及其索引值
     * @return boolean 找到为true、反之则false
     */
    private void ifExistUnderScript() {
    }

    private void ifExistSymbol() {
    }

    private void ifPureWord() {
    }
    
    /** 对{@code map}的键值进行翻转 */
    private Map<String, String> reverse(Map<String, String> map){
        Map<String, String> reversed = new HashMap<String, String>();
        Set<String> keys = map.keySet();
        for (String key: keys){
            String value = map.get(key);
            reversed.put(value, key);
        }
        return reversed;
    }
    
    // ***供公开方法使用 -- 审查 result数组中存储的各元素的合法性方法群
    private void checkPrefix() {
    }

    private void checkSuperScript() {
    }

    private void checkUnderScript() {
    }

    private void checkBehind() {
    }

    private void checkRepeatBhind() {
    }
    
    // private boolean check(){
    //     char[] charInput = input.toCharArray();//获取字符串每个字符
    //     String[] underWord = intersect(charInput, subWord); //相交、得到下加形态的字符串数组
    //     // 若存在下加字形态的字符,则可能存在上加字
    //     if (underWord.length > 0) {
    //         int indexOfUnderWord = input.indexOf(underWord[0]); //第一个下加字形态的索引值
    //         //如果发现 下加字 是 特殊字 的下加字部分、就要针对特殊字的可能情况做特殊处理
    //         if (Arrays.asList(new String[]{"ྙ", "ྥ", "ྷ"}).contains(underWord[0])) {   
    //             /** 当确认是特殊字时 需要执行的通用内部方法的类 */
    //             class SpecialUnder{
    //                 /** 当确认是特殊字时 需要执行的通用内部方法 */
    //                 void findAll(){
    //                     int index = indexOfUnderWord; // 找到了基字索引
    //                     // 基字我认为存储成整个 特殊字 效果会好，有待考证
    //                     result[BASE] = (String.valueOf(input.charAt(index-1)) + String.valueOf(input.charAt(index)));
    //                     // 沿着这个线索 寻找剩余的其他元素
    //                     find(index);
    //                     // 特殊字上下无加字情况，上下加字位置可以再次确认置空
    //                     result[SUPER_SCRIPT] = null;
    //                     result[UNDER_SCRIPT] = null;
    //                 }
    //             }
    //             SpecialUnder specialUnder = new SpecialUnder(); // 内部类的实例
    //             // 看看 下加字 上面的字符是不是 也是特殊字的组成部分 - 若是就特殊处理
    //             if (Arrays.asList(new String[]{"ག", "ད", "ཛ", "བ", "ཧ"}).contains(
    //                 String.valueOf(input.charAt(indexOfUnderWord-1)))) { 
    //                 if (underWord[0].equals("ྙ") & (String.valueOf(input.charAt(indexOfUnderWord-1))).equals("ཛ")) {
    //                     specialUnder.findAll();
    //                     return true;
    //                 }
    //                 else if (underWord[0].equals("ྥ") & (String.valueOf(input.charAt(indexOfUnderWord-1))).equals("ཧ")) {
    //                     specialUnder.findAll();
    //                     return true;
    //                 }
    //                 else if (underWord[0].equals("ྷ") & Arrays.asList(new String[]{"ག", "ད", "ཛ", "བ", "ཧ"}).contains(
    //                     String.valueOf(input.charAt(indexOfUnderWord-1)))) {
    //                     specialUnder.findAll();
    //                     return true;
    //                 }
    //             }// 虽然下加字是 特殊字 的组成部分 但字并不是特殊字的情况 
    //             else{ // 比如 རྙ（ཉ虽然是特殊字的组成部分，但是这里是基字）等情况    
    //                 int index = indexOfUnderWord;
    //                 // 基字 不能存储下字字形态的字符，需要以正常形态存储
    //                 result[BASE] = subWordMapReversed.get(String.valueOf(input.charAt(index)));
    //                 find(index); // 寻找其他元素
    //                 return true; 
    //             }
    //         }
    //         // 如果无特殊字干扰即为普遍情况
    //         else{
    //             int index = indexOfUnderWord;
    //             // 基字 不能存储下字字形态的字符，需要以正常形态存储
    //             result[BASE] = subWordMapReversed.get(String.valueOf(input.charAt(index)));
    //             // 寻找其他元素   //             find(index);
    //             return true;
    //         }
    //     }
    //     // 若 无 下加形态的字符、则不可能存在上加字, 即元素位置在该方法内找不到了
    //     else{ 
    //         return false;
    //     }
    // }


    // test main
    public static void main(String[] args) {
        Word word = new Word("རྒྲོགས་");
        // System.out.println(word.subWordMapReversed);
        
        word.ifExistSingle();
        word.ifExistSuperScript();
        for(String rst: word.result){
            System.out.println(rst);
    
        }
    }
}
