package com.lion.echart.project.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lion.echart.base.logic.BaseService;
import com.lion.echart.project.entity.MonthScheduleEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
public class MonthScheduleController {
	@Autowired
	private BaseService baseService;

	// 月进度计划编制查询所有
	@RequestMapping(value = "/project/monthscheduleList.web", method = RequestMethod.GET)
	public String monthScheduleList(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
		req.setAttribute("ts", System.currentTimeMillis());
		req.setAttribute("who", "project");
		return "/page/project/monthscheduleList";
	}

	// 获取进度反馈对比情况列表数据
	@RequestMapping("/project/monthscheduleData.json")
	@ResponseBody
	public List<Map<String, Object>> payforGetData( String date ) throws IOException {
		  Map<String, Object> param = new HashMap<String, Object>();
		  param.put("date", date);
		  List<Map<String, Object>> list = baseService.queryList("comle.monthschedule.monthscheduleData", param);
		return list;
	}
	
	
	//进度反馈对比保存
	//工程投资完成汇总补录数据保存
	@RequestMapping(value = "/project/MonthscheduleDataAdd.json",method=RequestMethod.POST)
	public @ResponseBody String insertFRepair(@RequestParam("a")String  data
			,HttpServletRequest req,HttpServletResponse resp, HttpSession session) throws Exception { 
		JSONObject obj = new JSONObject();
		Integer a=1;
		try {
		JSONArray jlist = JSONArray.fromObject(data);
		List<MonthScheduleEntity> list=new ArrayList<MonthScheduleEntity>();
		for(int i=0;i<jlist.size();i++) {
			MonthScheduleEntity entity = (MonthScheduleEntity) JSONObject.toBean((JSONObject) jlist.get(i), MonthScheduleEntity.class);
			if("".equals(entity.getDesignquantity())|| entity.getDesignquantity() ==null || "".equals(entity.getPlannedvolume())|| entity.getPlannedvolume() ==null) {
			}else {
				list.add(entity);
			}
		}
		baseService.insertOupdates("comle.monthschedule.monthschedule", list);
		} catch (Exception e) {
			e.printStackTrace();
			a=0;
		}
		obj.put("msgType", a);
		return obj.toString();
	}
	
}