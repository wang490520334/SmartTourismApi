package iii.org.tw.service;

import iii.org.tw.auth.SOCIAL;
import iii.org.tw.model.UserModel;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;






public class LdapService {

	private static Hashtable env = new Hashtable();
	private static String accountBaseDn = "ou=SmartTourism,dc=iii,dc=org,dc=tw";
	
	public static String activate = "st";
	public static String secureToken = "sn";
	public static String changeToken = "postalCode";
	public static String createDateLong = "carLicense";
	public static String imgSrc = "description";
	public static String name = "displayName";
	public static String mail = "mail";
	public static String telephone = "telephoneNumber";
	public static String mobile = "mobile";
	public static String gmail = "departmentNumber";
	public static String twitter = "employeeNumber";
	public static String facebook = "destinationIndicator";
	
	public static String gender = "employeeType";
	public static String age = "givenName";
	public static String nationality = "street";
	public static String education = "o";
	public static String industry = "ou";
	public static String marriage = "title";
	public static String monthlyIncome = "businessCategory";
	public static String timesToTaiwan = "telexNumber";
	
	

	static {
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, DbUtil.props.getProperty("ldap.PROVIDER_URL"));
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, DbUtil.props.getProperty("ldap.SECURITY_PRINCIPAL"));
		env.put(Context.SECURITY_CREDENTIALS, DbUtil.props.getProperty("ldap.SECURITY_CREDENTIALS"));
	}

	public static String createLdapUser(String username, String password, String secureToken) {

		try {

			String entryDN = "cn=" + username + "," + accountBaseDn;

			DirContext dirContext = null;
			// get a handle to an Initial DirContext
			dirContext = new InitialDirContext(env);

			// build the entry
			BasicAttributes entry = new BasicAttributes();
			entry.put(new BasicAttribute("cn", username));
			entry.put(new BasicAttribute("objectClass", "inetOrgPerson"));
			entry.put(new BasicAttribute("userPassword", LdapService.shaByte(password)));

			entry.put(new BasicAttribute(LdapService.activate, "false"));// 暫時利用st當是否啟動欄位
			entry.put(new BasicAttribute(LdapService.secureToken, secureToken));// 暫時利用sn當安全碼 (也作為userId好了)
			entry.put(new BasicAttribute(LdapService.createDateLong, String.valueOf(Calendar.getInstance().getTimeInMillis())));// 暫時利用carLicense當創建日期

			// Add the entry

			dirContext.createSubcontext(entryDN, entry);
			return "SUCCESS";

		} catch (NamingException e) {
			e.printStackTrace();
			System.err.println("AddUser: error adding entry." + e);
			return e.getMessage();
		}

	}
	
	public static String createLdapSocialUser(SOCIAL social, String userSocialId, String cn) throws SmartTourismException {

		try {

			String entryDN = "cn=" + cn + "," + accountBaseDn;

			DirContext dirContext = null;
			// get a handle to an Initial DirContext
			dirContext = new InitialDirContext(env);

			// build the entry
			BasicAttributes entry = new BasicAttributes();
			entry.put(new BasicAttribute("cn", cn));
			entry.put(new BasicAttribute("objectClass", "inetOrgPerson"));
			entry.put(new BasicAttribute("userPassword", LdapService.shaByte("xxxxx")));
			entry.put(new BasicAttribute(LdapService.secureToken, UUID.randomUUID().toString()));

			entry.put(new BasicAttribute(LdapService.activate, "true"));// 暫時利用st當是否啟動欄位
			entry.put(new BasicAttribute(LdapService.createDateLong, String.valueOf(Calendar.getInstance().getTimeInMillis())));// 暫時利用carLicense當創建日期
			
			
			switch (social) {
		        case GOOGLE:
		        	entry.put(new BasicAttribute(LdapService.gmail, userSocialId));
		            break;
		        case TWITTER:
		        	entry.put(new BasicAttribute(LdapService.twitter, userSocialId));   	
		            break;
		        case FACEBOOK:
		        	entry.put(new BasicAttribute(LdapService.facebook, userSocialId));
		            break;
			}			
			
			// Add the entry

			dirContext.createSubcontext(entryDN, entry);
			return "SUCCESS";

		} catch (NamingException e) {
			e.printStackTrace();
			System.err.println("AddUser: error adding entry." + e);
			throw new SmartTourismException("LDAP 創建失敗");
		}

	}
	
	
	
	public static String authenticate(String username, String password) {

		try {
			
			
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,  "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, DbUtil.props.getProperty("ldap.PROVIDER_URL"));
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, "cn="+username+",ou=SmartTourism,dc=iii,dc=org,dc=tw");
			env.put(Context.SECURITY_CREDENTIALS, password);

			// Create the initial context
			DirContext ctx = new InitialDirContext(env);

			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}
	
	public static SearchResult findOneByEmail(String email) throws SmartTourismException{

		try {
			
			
			DirContext dirContext = new InitialDirContext(env);
			String returnedAtts[] ={"cn", "objectClass", LdapService.secureToken, LdapService.activate, LdapService.name, LdapService.mail, LdapService.telephone, LdapService.mobile, LdapService.createDateLong, LdapService.changeToken, LdapService.imgSrc};
			SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);                 
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration namingEnumeration = dirContext.search(accountBaseDn, "(&(objectClass=inetOrgPerson)(cn="+ email + "))", searchCtls);

			
			if (namingEnumeration.hasMore()) {
				return (SearchResult)namingEnumeration.next();
			} else {
				return null;
			}


		} catch (Exception e) {
			e.printStackTrace();
			throw new SmartTourismException(e.getMessage());

		}

	}
	
	public static SearchResult findOneByUserId(String userId) throws SmartTourismException{

		try {
						
			DirContext dirContext = new InitialDirContext(env);
			String returnedAtts[] ={"cn", "objectClass", LdapService.secureToken, LdapService.activate, LdapService.name, LdapService.mail, LdapService.telephone, LdapService.mobile, LdapService.createDateLong, LdapService.changeToken, LdapService.imgSrc,  
					LdapService.gender, LdapService.age, LdapService.nationality, LdapService.education, LdapService.industry, LdapService.marriage, LdapService.monthlyIncome.concat(LdapService.timesToTaiwan)};
			
			
			SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);                 
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration namingEnumeration = dirContext.search(accountBaseDn, "(&(objectClass=inetOrgPerson)(sn="+ userId + "))", searchCtls);

			
			if (namingEnumeration.hasMore()) {
				return (SearchResult)namingEnumeration.next();
			} else {
				return null;
			}


		} catch (Exception e) {
			e.printStackTrace();
			throw new SmartTourismException(e.getMessage());

		}

	}
	
	
	public static SearchResult findOneBySocialUserId(SOCIAL social, String userSocialId) throws SmartTourismException{

		try {
						
			DirContext dirContext = new InitialDirContext(env);
			String returnedAtts[] ={"cn", "objectClass", LdapService.secureToken, LdapService.activate, LdapService.name, LdapService.mail, LdapService.telephone, LdapService.mobile, LdapService.createDateLong, LdapService.changeToken, LdapService.imgSrc};
			SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);                 
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            
            String socialAttr = null;
        	switch (social) {
    	        case GOOGLE:
    	        	socialAttr = LdapService.gmail;
    	            break;
    	        case TWITTER:
    	        	socialAttr = LdapService.twitter;
    	            break;
    	        case FACEBOOK:
    	        	socialAttr = LdapService.facebook;
    	            break;
        	}
            
            
			NamingEnumeration namingEnumeration = dirContext.search(accountBaseDn, "(&(objectClass=inetOrgPerson)("+ socialAttr +"="+ userSocialId + "))", searchCtls);

			
			if (namingEnumeration.hasMore()) {
				return (SearchResult)namingEnumeration.next();
			} else {
				return null;
			}


		} catch (Exception e) {
			e.printStackTrace();
			throw new SmartTourismException(e.getMessage());

		}

	}
	
	
	
	
	
	public static void modifyAttribute(String email, Map<String, String> attributes) throws SmartTourismException {

		try {
			
			String entryDN = "cn=" + email + "," + accountBaseDn;
			DirContext dirContext = new InitialDirContext(env);
			
			ModificationItem[] mods = new ModificationItem[attributes.size()];
			
			int index = 0;
			for (Map.Entry<String, String> entry : attributes.entrySet())
			{
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			    Attribute mod = new BasicAttribute(entry.getKey(), entry.getValue());
			    mods[index] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod);
			    index ++;
			}
			

//		    Attribute mod0 = new BasicAttribute("description", "true");
//		    Attribute mod1 = new BasicAttribute("telephoneNumber", "AAA");
//
//		    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod0);
//		    mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod1);

		    dirContext.modifyAttributes(entryDN, mods);



		} catch (Exception e) {
			e.printStackTrace();
			throw new SmartTourismException("資料格式有誤 或 資料不得為空");

		}

	}
	
	public static void updateUserDataByUserId (String userId, UserModel user) throws SmartTourismException {
		String mail = null;
    	SearchResult searchResult = LdapService.findOneByUserId(userId);
    	Attributes attrs = searchResult.getAttributes();
    	try {
    		mail = (String)attrs.get("cn").get();
	    	
		} catch (NamingException e) {
			e.printStackTrace();
		}
	    updateUserData(mail, user);
	}
	
	
	public static void updateUserData (String mail, UserModel user) throws SmartTourismException {
		Map<String, String> attributes = new HashMap<String, String>();
		
		if (!StringUtils.isEmpty(user.getName())) 
			attributes.put(LdapService.name, user.getName()); 
		if (!StringUtils.isEmpty(user.getMail())) 
			attributes.put(LdapService.mail, user.getMail()); 
		if (!StringUtils.isEmpty(user.getTelephone())) 
			attributes.put(LdapService.telephone, user.getTelephone()); 	
		if (!StringUtils.isEmpty(user.getMobile())) 
			attributes.put(LdapService.mobile, user.getMobile()); 
		
		
		if (!StringUtils.isEmpty(user.getGender())) 
			attributes.put(LdapService.gender, user.getGender()); 
		if (user.getAge() != 0) 
			attributes.put(LdapService.age, String.valueOf(user.getAge())); 
		if (!StringUtils.isEmpty(user.getNationality())) 
			attributes.put(LdapService.nationality, user.getNationality()); 
		if (!StringUtils.isEmpty(user.getEducation())) 
			attributes.put(LdapService.education, user.getEducation()); 
		if (!StringUtils.isEmpty(user.getIndustry())) 
			attributes.put(LdapService.industry, user.getIndustry()); 
		if (!StringUtils.isEmpty(user.getMarriage())) 
			attributes.put(LdapService.marriage, user.getMarriage()); 
		if (!StringUtils.isEmpty(user.getMonthlyIncome())) 
			attributes.put(LdapService.monthlyIncome, user.getMonthlyIncome()); 
		if (!StringUtils.isEmpty(user.getTimesToTaiwan())) 
			attributes.put(LdapService.timesToTaiwan, user.getTimesToTaiwan()); 


	    LdapService.modifyAttribute(mail, attributes);
	}
	
	public static void updateUserImgByUserId (String userId, String url) throws SmartTourismException {
		String mail = null;
    	SearchResult searchResult = LdapService.findOneByUserId(userId);
    	Attributes attrs = searchResult.getAttributes();
    	try {
    		mail = (String)attrs.get("cn").get();
	    	
		} catch (NamingException e) {
			e.printStackTrace();
		}
		updateUserImg(mail, url);
	}
	
	public static void updateUserImg (String mail, String url) throws SmartTourismException {
		Map<String, String> attributes = new HashMap<String, String>();
	    attributes.put(LdapService.imgSrc, url); 
	    LdapService.modifyAttribute(mail, attributes);
	}
	
	
	public static UserModel getUserData (String mail) throws SmartTourismException {
	    
	    UserModel user = new UserModel();
	    
	    SearchResult searchResult = LdapService.findOneByEmail(mail);
	    if (searchResult == null) {
	    	throw new SmartTourismException("帳號不存在");
	    } else {
	    	Attributes attrs = searchResult.getAttributes();
	    	try {
				
				user.setName(attrs.get(LdapService.name)==null ? null : (String)attrs.get(LdapService.name).get());
				user.setMail(attrs.get(LdapService.mail)==null ? null : (String)attrs.get(LdapService.mail).get());
				user.setTelephone(attrs.get(LdapService.telephone)==null ? null : (String)attrs.get(LdapService.telephone).get());
				user.setMobile(attrs.get(LdapService.mobile)==null ? null : (String)attrs.get(LdapService.mobile).get());
				user.setCreateDateLong(attrs.get(LdapService.createDateLong)==null ? null : (String)attrs.get(LdapService.createDateLong).get());
				user.setImgSrc(attrs.get(LdapService.imgSrc)==null ? null : (String)attrs.get(LdapService.imgSrc).get());
					
			} catch (Exception e) {
				e.printStackTrace();
				throw new SmartTourismException(e.getMessage());
			}
	    }
	    
	    return user;
	    
	    
	}
	
	public static UserModel getUserDataByUserId (String userId) throws SmartTourismException {
	    
	    UserModel user = new UserModel();
	    
	    SearchResult searchResult = LdapService.findOneByUserId(userId);
	    if (searchResult == null) {
	    	throw new SmartTourismException("帳號不存在");
	    } else {
	    	Attributes attrs = searchResult.getAttributes();
	    	try {
				
				user.setName(attrs.get(LdapService.name)==null ? null : (String)attrs.get(LdapService.name).get());
				user.setMail(attrs.get(LdapService.mail)==null ? null : (String)attrs.get(LdapService.mail).get());
				user.setTelephone(attrs.get(LdapService.telephone)==null ? null : (String)attrs.get(LdapService.telephone).get());
				user.setMobile(attrs.get(LdapService.mobile)==null ? null : (String)attrs.get(LdapService.mobile).get());
				user.setCreateDateLong(attrs.get(LdapService.createDateLong)==null ? null : (String)attrs.get(LdapService.createDateLong).get());
				user.setImgSrc(attrs.get(LdapService.imgSrc)==null ? null : (String)attrs.get(LdapService.imgSrc).get());
				
				
				user.setGender(attrs.get(LdapService.gender)==null ? null : (String)attrs.get(LdapService.gender).get());
				if (attrs.get(LdapService.age)!=null) {
					user.setAge(Integer.valueOf((String)attrs.get(LdapService.age).get()));
				}
				user.setNationality(attrs.get(LdapService.nationality)==null ? null : (String)attrs.get(LdapService.nationality).get());
				user.setEducation(attrs.get(LdapService.education)==null ? null : (String)attrs.get(LdapService.education).get());
				user.setIndustry(attrs.get(LdapService.industry)==null ? null : (String)attrs.get(LdapService.industry).get());
				user.setMarriage(attrs.get(LdapService.marriage)==null ? null : (String)attrs.get(LdapService.marriage).get());
				user.setMonthlyIncome(attrs.get(LdapService.monthlyIncome)==null ? null : (String)attrs.get(LdapService.monthlyIncome).get());
				user.setTimesToTaiwan(attrs.get(LdapService.timesToTaiwan)==null ? null : (String)attrs.get(LdapService.timesToTaiwan).get());

					
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	    }
	    
	    return user;
	    
	    
	}
	
	

	public static String shaByte(String pass) {
		MessageDigest sha;
		try {
			  
            sha = MessageDigest.getInstance("SHA");
            sha.update(pass.getBytes());
            byte[] hash = sha.digest();
            String pswSHA = "{SHA}" + new String(Base64.encodeBase64(hash));
           
			return pswSHA;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
