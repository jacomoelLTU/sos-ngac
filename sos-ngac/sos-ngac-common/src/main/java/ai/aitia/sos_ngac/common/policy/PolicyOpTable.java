package ai.aitia.sos_ngac.common.policy;

import java.util.Map;
import static java.util.Map.entry;

import java.util.HashMap; 

/* 
 * All implemented API calls of the NGAC policy query- and admin interfaces.
 * The policy server will validate incoming requests with this table;
 * hence, a policy request DTO should be sent with a defined operation
 * constant and its corresponding parameters listed in this table. 
 * 
 */
public class PolicyOpTable {
	
	public static final Map<String, String[]> table = Map.ofEntries(
			entry(PolicyOpConstants.GET_POLICY, new String[]{
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.SET_POLICY, new String[]{
					PolicyOpConstants.PARAM_POLICY,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.ADD_ELEMENT, new String[]{
					PolicyOpConstants.PARAM_POLICY, 
					PolicyOpConstants.PARAM_POLICY_ELEMENT,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.ADD_MULTIPLE_ELEMENTS, new String[]{
					PolicyOpConstants.PARAM_POLICY, 
					PolicyOpConstants.PARAM_POLICY_ELEMENTS,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.DELETE, new String[]{
					PolicyOpConstants.PARAM_POLICY, 
					PolicyOpConstants.PARAM_POLICY_ELEMENT,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.DELETE_MULTIPLE_ELEMENTS, new String[]{
					PolicyOpConstants.PARAM_ADMIN_TOKEN,
					PolicyOpConstants.PARAM_POLICY, 
					PolicyOpConstants.PARAM_POLICY_ELEMENTS,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.COMBINE_POLICIES, new String[]{
					PolicyOpConstants.PARAM_POLICY_1, 
					PolicyOpConstants.PARAM_POLICY_2,
					PolicyOpConstants.PARAM_COMBINE_POLICY_IDENTIFIER,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.LOAD_POLICY, new String[]{
					PolicyOpConstants.PARAM_POLICY_FILENAME,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.LOAD_POLICY_IMMEDIATE, new String[]{
					PolicyOpConstants.PARAM_POLICY_SPECIFIER,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.UNLOAD_POLICY, new String[]{
					PolicyOpConstants.PARAM_POLICY,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.READ_POLICY, new String[]{
					PolicyOpConstants.PARAM_POLICY,
					PolicyOpConstants.PARAM_ADMIN_TOKEN
					}),
			entry(PolicyOpConstants.ACCESS_QUERY, new String[]{
					PolicyOpConstants.PARAM_USER,
					PolicyOpConstants.PARAM_ACCESS_RIGHT,
					PolicyOpConstants.PARAM_OBJECT
					}),
			entry(PolicyOpConstants.CONDITIONAL_ACCESS_QUERY, new String[]{
					PolicyOpConstants.PARAM_USER,
					PolicyOpConstants.PARAM_ACCESS_RIGHT,
					PolicyOpConstants.PARAM_OBJECT,
					PolicyOpConstants.PARAM_CONDITION
					})
			);
	
	public PolicyOpTable() {
		throw new UnsupportedOperationException();
	}
}
