package com.zzzwb.myblog.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zzzwb.myblog.bean.SearchBean;
import com.zzzwb.myblog.constant.Direction;
import com.zzzwb.myblog.domain.base.BaseEntity;
import com.zzzwb.myblog.exception.ParamErrorException;
import com.zzzwb.myblog.service.base.BaseService;
import com.zzzwb.myblog.tkmybatis.BaseMapper;
import com.zzzwb.myblog.web.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 事务层基类
 * 附带通用的增删改查方法
 * 泛型为实体
 *
 * @author zeng wenbin
 * @date Created in 2019/7/25
 */
public class BaseServiceImpl<E extends BaseEntity> implements BaseService<E> {
	/**
	 * 持久化层对象
	 */
	private BaseMapper<E> baseMapper;
	/**
	 * 泛型字节码文件对象
	 */
	private Class entityClass;


	/**
	 * == 查询
	 */
	private static final String EQ = "eq";
	/**
	 * != 查询
	 */
	private static final String NOT_EQ = "not_eq";
	/**
	 * like 查询
	 */
	private static final String LIKE = "like";
	/**
	 * not like 查询
	 */
	private static final String NOT_LIKE = "not_like";
	/**
	 * < 查询
	 */
	private static final String LT = "lt";
	/**
	 * <= 查询
	 */
	private static final String LE = "le";
	/**
	 * > 查询
	 */
	private static final String GT = "ge";
	/**
	 * >= 查询
	 */
	private static final String GE = "gt";
	/**
	 * in 查询
	 */
	private static final String IN = "in";
	/**
	 * not in 查询
	 */
	private static final String NOT_IN = "not_in";
	/**
	 * between 查询
	 */
	private static final String BTW = "btw";
	/**
	 * not between 查询
	 */
	private static final String NOT_BTW = "not_btw";
	/**
	 * is null 查询
	 */
	private static final String IS_NULL = "is_null";
	/**
	 * is not null 查询
	 */
	private static final String NOT_NULL = "not_null";

	public BaseServiceImpl(BaseMapper<E> baseMapper) {
		this.baseMapper = baseMapper;
		this.getEClass();
	}

	/**
	 * 获取泛型E的字节码文件对象
	 */
	private void getEClass() {
		this.entityClass = null;
		Class c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			this.entityClass = (Class<E>) parameterizedType[0];
		}else {
			this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}


	@Override
	public Optional<E> findOne(Integer id) {
		return Optional.ofNullable(this.baseMapper.selectByPrimaryKey(id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteOne(Integer id) {
		this.baseMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteList(List<Integer> ids) {
		Example example = new Example(this.entityClass);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);
		this.baseMapper.deleteByExample(example);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Optional<E> update(E entity) {
		int i = this.baseMapper.updateByPrimaryKey(entity);
		if (i == 1) {
			return Optional.of(this.baseMapper.selectByPrimaryKey(entity.getId()));
		}
		return Optional.empty();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public E create(E entity) {
		this.baseMapper.insert(entity);
		return entity;
	}

	@Override
	public List<E> findAll() {
		return this.baseMapper.selectAll();
	}

	@Override
	public PageInfo<E> queryPage(PageVo pageVo) throws ParamErrorException {
		Example example = this.getExample();
		Example.Criteria criteria = example.createCriteria();
		List<String> propertyNames = pageVo.getPropertyNames();
		List<Object> propertyValues = pageVo.getPropertyValues();
		List<String> sortNames = pageVo.getSortNames();
		List<Direction> sortValues = pageVo.getSortValues();
		if (propertyNames.size() != propertyValues.size() || sortNames.size() != sortValues.size()) {
			//字段名和字段值  或者 参数名和参数值不对应 抛异常
			throw new ParamErrorException("page", "搜索条件名称和值不对应或排序名称和值不对应");
		}
		//封装搜索条件
		this.packageSearch(criteria, propertyNames, propertyValues);
		//封装排序
		this.packageSort(example,sortNames,sortValues);
		//使用pageHelper后的第一次select查询会增加limit限制进行分页查询
		PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
		return new PageInfo<E>(this.baseMapper.selectByExample(example));
	}

	@Override
	public List<E> queryBySearch(SearchBean searchBean) {
		Example example = this.getExample();
		this.packageSearch(example.createCriteria(), searchBean.getPropertyNames(), searchBean.getPropertyValues());
		return this.baseMapper.selectByExample(example);
	}

	/**
	 * 封装排序规则
	 * 		由于example.setOrderByClause方法中使用的是列名
	 * 		所以需要将属性名转换为列名，驼峰命名法 -> sql列名命名规则
	 * @param example example
	 * @param sortNames 排序名称集合
	 * @param sortValues 排序值集合
	 */
	private void packageSort(Example example, List<String> sortNames, List<Direction> sortValues) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sortNames.size(); i++) {
			//此处需要将属性名转换为表中的列名
			StringBuffer buffer = new StringBuffer();
			char[] nameChars = sortNames.get(i).toCharArray();
			for (char nameChar : nameChars) {
				if (nameChar >= 'A' && nameChar <= 'Z'){
					//将大写字母转换为下划线和对应的小写字母组合
					buffer.append("_").append(String.valueOf(nameChar).toLowerCase());
				} else {
					buffer.append(String.valueOf(nameChar));
				}
			}
			builder.append("`").append(buffer).append("` ").append(sortValues.get(i).name()).append(",");
		}
		example.setOrderByClause(builder.substring(0, builder.length()-1));
	}


	/**
	 * 获取Example实例
	 * 		供子类重写，子类创建Example实例后手工添加一些条件如：要获取哪些列，是否去重等
	 * @return Example实例
	 */
	protected Example getExample() {
		return new Example(this.entityClass);
	}

	/**
	 * 封装搜索条件
	 *
	 * @param criteria       criteria
	 * @param propertyNames  字段名集合
	 * @param propertyValues 字段值集合
	 */
	protected void packageSearch(Example.Criteria criteria, List<String> propertyNames, List<Object> propertyValues) {
		for (int i = 0; i < propertyNames.size(); i++) {
			String name = propertyNames.get(i);
			Object value = propertyValues.get(i);
			String result = this.dealBySelf(criteria, name, value);
			//判断是否被子类处理，被处理遍历下一条数据
			if (StringUtils.isNotEmpty(result)) {
				continue;
			}
			String[] nameSplit = name.split("-");
			if (nameSplit.length == 1) {
				//没有检索方式默认使用equal方式
				criteria.andEqualTo(name, value);
				return;
			}
			String mode = nameSplit[1];
			switch (mode) {
				case EQ:
					criteria.andEqualTo(nameSplit[0], value);
					break;
				case NOT_EQ:
					criteria.andNotEqualTo(nameSplit[0], value);
					break;
				case LIKE:
					criteria.andLike(nameSplit[0], "%"+value.toString()+"%");
					break;
				case NOT_LIKE:
					criteria.andNotLike(nameSplit[0], value.toString());
					break;
				case LE:
					criteria.andLessThanOrEqualTo(nameSplit[0], value);
					break;
				case LT:
					criteria.andLessThan(nameSplit[0], value);
					break;
				case GE:
					criteria.andGreaterThanOrEqualTo(nameSplit[0], value);
					break;
				case GT:
					criteria.andGreaterThan(nameSplit[0], value);
					break;
				case IN:
					criteria.andIn(nameSplit[0], Lists.newArrayList(value.toString().split(",")));
					break;
				case NOT_IN:
					criteria.andNotIn(nameSplit[0], Lists.newArrayList(value.toString().split(",")));
					break;
				case BTW:
					String[] valueSplit = value.toString().split(",");
					criteria.andBetween(nameSplit[0], valueSplit[0], valueSplit[1]);
					break;
				case NOT_BTW:
					String[] valueSplit2 = value.toString().split(",");
					criteria.andBetween(nameSplit[0], valueSplit2[0], valueSplit2[1]);
					break;
				case IS_NULL:
					criteria.andIsNull(nameSplit[0]);
					break;
				case NOT_NULL:
					criteria.andIsNotNull(nameSplit[0]);
					break;
				default:
					//全部不匹配进行equal比较
					criteria.andEqualTo(name, value);
			}
		}
	}

	/**
	 * 供子类重写的方法
	 * 当子类需要对特殊字段进行单独处理的时候，可以重写此方法，并返回字符串"ok"
	 *
	 * @param criteria criteria
	 * @param name     字段名
	 * @param value    字段值
	 * @return ok或null
	 */
	protected String dealBySelf(Example.Criteria criteria, String name, Object value) {
		return null;
	}
}
