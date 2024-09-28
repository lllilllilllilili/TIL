class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> nums_to_index = new HashMap<>();
        for (int i=0; i<nums.length; i++) {
            int a = target - nums[i];
            if (nums_to_index.containsKey(a)) {
                return new int[]{nums_to_index.get(a), i};
            }
            nums_to_index.put(nums[i], i);
        }
        return new int[]{};
    }
}

//target 에서 nums[i] 를 빼야 함 
//key, values 를 활용해야함 