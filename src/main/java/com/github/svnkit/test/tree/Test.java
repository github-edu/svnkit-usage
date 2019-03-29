package com.github.svnkit.test.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Test {
	
	static long COUNT = 0;
	
	public static void main(String[] args) {
		List<User> users = new ArrayList<User>();
		
		for (int i = 0; i < 10; i++) {
			users.add(new User("10000" + i, null, UUID.randomUUID().toString()));
		}
		
		users.add(new User("1", null, "zhangsan"));
		
		users.add(new User("2", "1", "zhangsan-s1"));
		users.add(new User("3", "1", "zhangsan-s2"));
		users.add(new User("4", "1", "zhangsan-s3"));
		users.add(new User("5", "1", "zhangsan-s4"));
		
		users.add(new User("21", "2", "lisi-1"));
		users.add(new User("22", "2", "lisi-2"));
		users.add(new User("23", "2", "lisi-3"));

		users.add(new User("51", "5", "saomao-1"));
		users.add(new User("52", "5", "saomao-2"));
		
		
		User user = getUserWithChildren(users, "1");
		System.out.println(user);
		
		System.out.println(COUNT);
	}
	
	
	public static User getUserWithChildren(List<User> users, String id) {
		if (null == id || id.trim().isEmpty() || null == users || users.isEmpty()) {
			return null;
		}
		
		User target = null;
		Map<String, User> map = new HashMap<String, User>();
		for (User user : users) {
			COUNT ++;
			if (id.equals(user.id)) {
				target = user;
			} else {
				map.put(user.id, user);
			}
		}
		if (null == target) {
			return null;
		}
		setChildren(target, map);
		return target;
	}


	private static void setChildren(User target, Map<String, User> map) {
		
		target.children.clear();
		map.forEach((id, user) -> {
			COUNT ++;
			if (target.id.equals(user.pid)) {
				target.children.add(user);
			}
		});
		
		target.children.forEach(user -> {
			COUNT ++;
			map.remove(user.id);
		});
		
		target.children.forEach(user -> {
			COUNT ++;
			setChildren(user, map);
		});
		
		
	}



}


class User {
	
	public String id;
	public String pid;
	public String name;
	
	public List<User> children = new ArrayList<>();
	
	public User() {
		super();
	}

	public User(String id, String pid, String name) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", pid=" + pid + ", name=" + name + ", children=" + children + "]";
	}
	
}
