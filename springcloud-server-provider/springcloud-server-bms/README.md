Spring Data JPA
==============

官方文档：

[https://docs.spring.io/spring-data/jpa/docs/2.0.14.RELEASE/reference/html/](https://docs.spring.io/spring-data/jpa/docs/2.0.14.RELEASE/reference/html/)

## 基本使用

领域驱动模型开发(DDD)，对比Mybatis和JPA : [如何对 JPA 或者 MyBatis 进行技术选型](http://www.spring4all.com/article/391)

spring-boot引入

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-data-jpa</artifactId>
 </dependency>
```

定义实体`@entity`：

```java
@Entity
public class User {

  @Id
  @GeneratedValue
  Long id;

  String lastname;
}
```

定义仓库`@Repository`:

```java
@Repository
public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {


    /**
     * 根据lastname查找
     * @param lastname
     * @return
     */
    List<User> findByLastname(String lastname);
}
```



### 注解使用说明

Java Persistence API定义了一种定义，可以将常规的普通Java对象（有时被称作POJO）映射到数据库。

Java Persistence API还定义了一种查询语言（JPQL），具有与SQL相类似的特征，只不过做了裁减，以便处理Java对象而非原始的关系表。

* **@Entity** 表明该类为一个实体类，属性name（表名）

* **@Table** 当实体类与其映射的数据库表名不同名时需要使用 @Table注解说明，与 @Entity 注解并列使用。属性name （表名） 、catalog （数据库目录）、schema（数据库名）

  一个数据库系统包含多个Catalog，每个Catalog包含多个Schema，每个Schema包含多个数据库对象（表、视图、字段等）

  | 数据库 | Catalog支持 | Schema支持     |
  | ------ | ----------- | -------------- |
  | Oracle | 不支持      | Oracle User ID |
  | MySQL  | 不支持      | 数据库名       |

* **@Column**注释定义了将成员属性映射到关系表中的哪一列和该列的结构信息。name：映射的列名，unique：是否唯一，nullable：是否允许为空，length：对于字符型列，length属性指定列的最大字符长度；

* **@Id** 表的主键，@Id一起使用的还有另外两个注解：@GeneratedValue、@GenericGenerator。SqlServer对应identity，MySQL 对应 auto increment。



### 主键生成策略修改

采用32位uuid生成主键

```java
@Entity
@Table(name = "ip_user")
public class User  implements Serializable {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @Column(length = 32)
    private String userId;
    ...
}
/** 
@GenericGenerator(name = "jpa-uuid", strategy = "uuid") 
@GeneratedValue(generator = "jpa-uuid") 
两个注解是生成策略核心注解。
**/
```



### 创建查询：Query Creation



根据`方法名`创建查询，实现字段自动查询。

[https://docs.spring.io/spring-data/jpa/docs/2.0.14.RELEASE/reference/html/#repositories.query-methods.query-creation](https://docs.spring.io/spring-data/jpa/docs/2.0.14.RELEASE/reference/html/#repositories.query-methods.query-creation)

| Keyword             | Sample                                                       | JPQL snippet                                                 |
| :------------------ | :----------------------------------------------------------- | :----------------------------------------------------------- |
| `And`               | `findByLastnameAndFirstname`                                 | `… where x.lastname = ?1 and x.firstname = ?2`               |
| `Or`                | `findByLastnameOrFirstname`                                  | `… where x.lastname = ?1 or x.firstname = ?2`                |
| `Is,Equals`         | `findByFirstname`,`findByFirstnameIs`,`findByFirstnameEquals` | `… where x.firstname = ?1`                                   |
| `Between`           | `findByStartDateBetween`                                     | `… where x.startDate between ?1 and ?2`                      |
| `LessThan`          | `findByAgeLessThan`                                          | `… where x.age < ?1`                                         |
| `LessThanEqual`     | `findByAgeLessThanEqual`                                     | `… where x.age <= ?1`                                        |
| `GreaterThan`       | `findByAgeGreaterThan`                                       | `… where x.age > ?1`                                         |
| `GreaterThanEqual`  | `findByAgeGreaterThanEqual`                                  | `… where x.age >= ?1`                                        |
| `After`             | `findByStartDateAfter`                                       | `… where x.startDate > ?1`                                   |
| `Before`            | `findByStartDateBefore`                                      | `… where x.startDate < ?1`                                   |
| `IsNull`            | `findByAgeIsNull`                                            | `… where x.age is null`                                      |
| `IsNotNull,NotNull` | `findByAge(Is)NotNull`                                       | `… where x.age not null`                                     |
| `Like`              | `findByFirstnameLike`                                        | `… where x.firstname like ?1`                                |
| `NotLike`           | `findByFirstnameNotLike`                                     | `… where x.firstname not like ?1`                            |
| `StartingWith`      | `findByFirstnameStartingWith`                                | `… where x.firstname like ?1`(parameter bound with appended `%`) |
| `EndingWith`        | `findByFirstnameEndingWith`                                  | `… where x.firstname like ?1`(parameter bound with prepended `%`) |
| `Containing`        | `findByFirstnameContaining`                                  | `… where x.firstname like ?1`(parameter bound wrapped in `%`) |
| `OrderBy`           | `findByAgeOrderByLastnameDesc`                               | `… where x.age = ?1 order by x.lastname desc`                |
| `Not`               | `findByLastnameNot`                                          | `… where x.lastname <> ?1`                                   |
| `In`                | `findByAgeIn(Collection<Age> ages)`                          | `… where x.age in ?1`                                        |
| `NotIn`             | `findByAgeNotIn(Collection<Age> ages)`                       | `… where x.age not in ?1`                                    |
| `True`              | `findByActiveTrue()`                                         | `… where x.active = true`                                    |
| `False`             | `findByActiveFalse()`                                        | `… where x.active = false`                                   |
| `IgnoreCase`        | `findByFirstnameIgnoreCase`                                  | `… where UPPER(x.firstame) = UPPER(?1)`                      |

### @Query注解（Using @Query）

自定义查询语句，使用该注解有两种方式，一种是JPQL的SQL语言方式，一种是原生SQL的语言

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.emailAddress = ?1")
  User findByEmailAddress(String emailAddress);
    
  /**
   * 一个参数，匹配两个字段
   * @param name2
   * @return
   * 这里Param的值和=:后面的参数匹配，但不需要和方法名对应的参数值对应
   */
  @Query("select c from User c where c.firstName=:name or c.lastName=:name  order by c.id desc")
  List<User> findByName(@Param("name") String name2);
    
   /** 这里的%只能放在占位的前面，后面不行 */ 
   @Query("select c from Customer c where c.firstName like %?1")
    
   /**  开启nativeQuery=true，在value里可以用原生SQL语句完成查询 */
   @Query(nativeQuery = true,value = "select * from Customer c where c.first_name like concat('%' ,?1,'%') ")

}

```

可能看了上面的代码有些疑惑，这里做一下解释：

* ？加数字表示占位符，？1代表在方法参数里的第一个参数，区别于其他的index，这里从1开始
* =: 加上变量名，这里是与方法参数中有@Param的值匹配的，而不是与实际参数匹配的。
* JPQL的语法中，表名的位置对应Entity的名称，字段对应Entity的属性,详细语法见相关文档
  要使用原生SQL需要在@Query注解中设置nativeQuery=true，然后value变更为原生SQL即可



## 高级使用









## 服务部署

```shell
# 打包忽略测试 ：-Dmaven.test.skip=true
mvn -DskipTests  -U clean package 
```

