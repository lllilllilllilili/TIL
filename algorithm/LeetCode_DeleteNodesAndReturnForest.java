/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        //정답
        List<TreeNode> result = new ArrayList<>();
        //삭제 대상이 되는 애를 담는 그릇
        Set<Integer> deleteSets = new HashSet<>(); //중복 방지
        for(int val : to_delete) {
            deleteSets.add(val);
        }

        if(!deleteSets.contains(root.val)) {
            //만약 삭제대상이 아니라면
            result.add(root);
        }

        deleteNodes(root, deleteSets, result);
        return result;
    }

    private TreeNode deleteNodes(TreeNode root, Set<Integer> deleteSets, List<TreeNode> result) {

        if (root == null) {
            return null;
        }

        root.left = deleteNodes(root.left, deleteSets, result);
        root.right = deleteNodes(root.right, deleteSets, result);
        if (deleteSets.contains(root.val)) {
            if (root.left != null) {
                result.add(root.left);
            }
            if (root.right != null) {
                result.add(root.right);
            }
            return null;
        }
        return root;
    }
}
