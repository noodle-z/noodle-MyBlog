package com.zzzwb.myblog.web.rest;

import com.zzzwb.myblog.annotation.Access;
import com.zzzwb.myblog.constant.Action;
import com.zzzwb.myblog.domain.base.BaseEntity;
import com.zzzwb.myblog.exception.ParamErrorException;
import com.zzzwb.myblog.service.base.BaseService;
import com.zzzwb.myblog.web.dto.BaseDto;
import com.zzzwb.myblog.web.vo.PageVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * rest资源前端接口基类
 * 基于restful风格附带通用的增删改查方法
 * 泛型为实体
 *
 * @author zeng wenbin
 * @date Created in 2019/7/26
 */
public class BaseResource<E extends BaseEntity> {


	/**
	 * 事务层对象
	 */
	private BaseService<E> baseService;

	public BaseResource(BaseService<E> baseService) {
		this.baseService = baseService;
	}

	@GetMapping("/{id}")
	@ApiOperation("根据id查询单个")
	@Access(value = Action.SELECT, description = "查询单个")
	public ResponseEntity<BaseDto> findOne(@PathVariable Integer id, HttpServletRequest request) {
		Optional<E> one = this.baseService.findOne(id);
		HttpSession session = request.getSession();
		return one.map(e -> ResponseEntity.ok(BaseDto.success(e))).orElseGet(() -> ResponseEntity.ok(BaseDto.error(
				"数据不存在")));
	}

	@GetMapping
	@ApiOperation("分页查询")
	@Access(value = Action.SELECT, description = "分页查询")
	public ResponseEntity<BaseDto> page(@ApiParam PageVo pageVo) {
		try {
			return ResponseEntity.ok(BaseDto.success(this.baseService.queryPage(pageVo)));
		} catch (ParamErrorException e) {
			e.printStackTrace();
			return ResponseEntity.ok(BaseDto.error("搜索条件名称和值不对应或排序名称和值不对应"));
		}
	}

	@DeleteMapping("/{id}")
	@ApiOperation("根据id删除单个")
	@Access(value = Action.DELETE, description = "删除单个")
	public ResponseEntity<BaseDto> deleteOne(@PathVariable Integer id) {
		this.baseService.deleteOne(id);
		return ResponseEntity.ok(BaseDto.success());
	}

	@DeleteMapping
	@ApiOperation("根据id删除多个")
	@Access(value = Action.DELETE, description = "批量删除")
	public ResponseEntity<BaseDto> deletes(@RequestParam List<Integer> ids) {
		this.baseService.deleteList(ids);
		return ResponseEntity.ok(BaseDto.success());
	}

	@PutMapping
	@ApiOperation("数据更新")
	@Access(value = Action.UPDATE, description = "更新")
	public ResponseEntity<BaseDto> update(@RequestBody E entity) {
		Optional<E> update = this.baseService.update(entity);
		return update.map(data -> ResponseEntity.ok(BaseDto.success(data))).orElseGet(() -> ResponseEntity.ok(BaseDto.error("数据不存在")));
	}

	@PostMapping
	@ApiOperation("新增")
	@Access(value = Action.ADD, description = "新增")
	public ResponseEntity<BaseDto> create(@Valid @RequestBody E entity){
		E e = this.baseService.create(entity);
		return ResponseEntity.ok(BaseDto.success(e));
	}

}
