package com.atai.ataiservice.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.atai.ataiservice.client.OssClient;
import com.atai.ataiservice.entity.AtaiCompetition;
import com.atai.ataiservice.entity.AtaiCompetitionRecord;
import com.atai.ataiservice.entity.AtaiCompetitionTeam;
import com.atai.ataiservice.entity.AtaiTeamUser;
import com.atai.ataiservice.entity.vo.CompetitionQuery;
import com.atai.ataiservice.mapper.AtaiCompetitionMapper;
import com.atai.ataiservice.service.AtaiCompetitionRecordService;
import com.atai.ataiservice.service.AtaiCompetitionService;
import com.atai.ataiservice.service.AtaiCompetitionTeamService;
import com.atai.ataiservice.service.AtaiTeamUserService;
import com.atai.ataiservice.utils.PythonRunner;
import com.atai.commonutils.result.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * <p>
 * 比赛 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
@Service
public class AtaiCompetitionServiceImpl extends ServiceImpl<AtaiCompetitionMapper, AtaiCompetition> implements AtaiCompetitionService {

    @Autowired
    private AtaiCompetitionService ataiCompetitionService;
    @Autowired
    private AtaiCompetitionRecordService ataiCompetitionRecordService;
    @Autowired
    private AtaiCompetitionTeamService ataiCompetitionTeamService;
    @Autowired
    private AtaiTeamUserService ataiTeamUserService;
    @Autowired
    private OssClient ossClient;

    //1   分页查询比赛的方法
    @Override
    public Map<String, Object> getCompetitionPageList(Page<AtaiCompetition> pageComp, CompetitionQuery competitionQuery) {
        QueryWrapper<AtaiCompetition> wrapper = new QueryWrapper<>();

        //判断条件值是否为空，不为空拼接
        if (!StringUtils.isEmpty((competitionQuery.getName()))) {//关键字
            wrapper.like("name", competitionQuery.getName());
        }
        if (!StringUtils.isEmpty(competitionQuery.getLevel())) { //分类
            wrapper.eq("level", competitionQuery.getLevel());
        }
        if (!StringUtils.isEmpty(competitionQuery.getTech())) { //分类
            wrapper.eq("tech", competitionQuery.getTech());
        }
        wrapper.eq("is_large", 0); // 展示的不是大型比赛

        Date date = new Date();
        if (competitionQuery.getStatus() == 1) {
            wrapper.gt("deadline", date);
        } else if (competitionQuery.getStatus() == 2) {
            wrapper.lt("deadline", date);
        }

        if ("最新".equals(competitionQuery.getSort())) {
            wrapper.orderByAsc("gmt_create");
        } else if ("最热".equals(competitionQuery.getSort())) {
            wrapper.orderByDesc("participants");
        }

        //把分页数据封装到pageComp对象里去
        baseMapper.selectPage(pageComp, wrapper);

        List<AtaiCompetition> records = pageComp.getRecords();
        long current = pageComp.getCurrent();
        long pages = pageComp.getPages();
        long size = pageComp.getSize();
        long total = pageComp.getTotal();
        boolean hasNext = pageComp.hasNext();//下一页
        boolean hasPrevious = pageComp.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    public String saveFile(MultipartFile multipartFile) {
        //需要保存的路径
        String uploadPath = "D:\\Projects\\JavaProjects\\ATAI_BigData_Backend\\service\\service_atai\\src\\main\\resources\\upload\\";
        String today = DateUtil.today();
        uploadPath += today;
        //获取源文件名
        String originalFilename = multipartFile.getOriginalFilename();
        //为文件生成唯一的uuid
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String uuid = snowflake.nextIdStr();
        //创建空文件
        File file = new File(uploadPath + File.separator + uuid + originalFilename);
        //保存之后的文件路径
        String absolutePath = null;
        try {
            absolutePath = file.getCanonicalPath();
            /*判断路径中的文件夹是否存在，如果不存在，先创建文件夹*/
            String dirPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //获取multipart文件输入流
            InputStream ins = multipartFile.getInputStream();
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            //写入文件
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return absolutePath;
    }

    @Override
    public void submit(MultipartFile file, String competitionId, String teamId, String userId) {
        AtaiCompetition competition = ataiCompetitionService.getById(competitionId);
        String resultUrl = competition.getResult();
        Integer submitType = competition.getSubmitType();

        R res = ossClient.getFile(resultUrl);
        ArrayList<String> resList = (ArrayList<String>) res.getData().get("resList");

        //1 上传提交的文件,为以后查看使用
        R r = ossClient.uploadOssFile(file);
        String submitUrl = (String) r.getData().get("url");

        int count = 0; // 结果正确的数量
        double score = 0;

        //2 统计结果正确的个数
        if (submitType == 1) {  // 处理代码文件
            String pythonPath = saveFile(file); // 将文件保存到本地并获取保存的地址
            ArrayList<String> output = PythonRunner.run(pythonPath); // 运行保存的python文件
            for (int i = 0; i < resList.size() & i < output.size(); i++) {
                if (Objects.equals(resList.get(i), output.get(i))) {
                    count++;
                }
            }
        } else if (submitType == 0) {   // 处理普通文件
            try {
                InputStream in = file.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String tmp;
                for (String resTmp : resList) {
                    if ((tmp = br.readLine()) != null) {
                        if (tmp.equals(resTmp)) count++;
                    } else break;
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        score = (double) count / resList.size() * 100; //3 计算得分

        //4 保存record对象
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String id = snowflake.nextIdStr();
        Date date = DateUtil.date();
        AtaiCompetitionRecord record = new AtaiCompetitionRecord
                (id, competitionId, teamId, userId, file.getOriginalFilename(), submitUrl, score, 0, date, date);
        ataiCompetitionRecordService.save(record);

        //5 更新competitionTeam对象
        AtaiCompetitionTeam competitionTeam = ataiCompetitionTeamService.getById(teamId);
        Double oldScore = competitionTeam.getScore();
        if (score > oldScore) {
            competitionTeam.setScore(score);
            competitionTeam.setBestTime(date);
            ataiCompetitionTeamService.updateById(competitionTeam);
        }
    }

    @Override
    public List<AtaiCompetition> getLargeCompetition() {
        QueryWrapper<AtaiCompetition> wrapper = new QueryWrapper<>();
        wrapper.eq("is_large", 1);
        wrapper.last("limit 5");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<AtaiCompetition> getListByUserId(String userId) {
        List<AtaiTeamUser> userCompetitions = ataiTeamUserService.getCompetitionByUserId(userId);
        ArrayList<AtaiCompetition> list = new ArrayList<>();
        for (AtaiTeamUser userCompetition : userCompetitions) {
            String competitionId = userCompetition.getCompetitionId();
            AtaiCompetition competition = ataiCompetitionService.getById(competitionId);
            list.add(competition);
        }
        return list;
    }

}
