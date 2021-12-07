import re
import requests
import pymysql

# 翻页处理
url = {}
for m in range(0, 73):
    if m == 0:
        url[m] = "http://teach.dlut.edu.cn/zytg/zytg.htm"
    else:
        url[m] = 'http://teach.dlut.edu.cn/zytg/zytg/{}.htm'.format(73 - m)

# 爬取网页源代码
    resp01 = requests.get(url=url[m])
    resp01.encoding = 'UTF-8'
    htmlCode = resp01.text

# 正则表达式缓存
    reCode_01 = re.compile(r'<UL>.*?</UL>', re.S)
    reCode_02 = re.compile(r'<a href="(?P<news_link>.*?)'
                           r'" target="_blank" title="(?P<news_name>.*?)'
                           r'">.*?<SPAN>(?P<news_time>.*?)</SPAN>', re.S)

# 连接到MySQL数据库存储
    newsNameList = []
    newsTimeList = []
    newsLinkList = []
    database01 = pymysql.connect(host='127.0.0.1', port=3306,
                                 user='test', password='4050',
                                 database="lu01", charset='utf8')
    cursor01 = database01.cursor()

# 第1次查询，使用reCode_01正则表达式，单独获取含有通知名称等信息的主体内容
    result01 = reCode_01.finditer(htmlCode)

    for i in result01:
        # 第2次查询，使用reCode_02正则表达式，从主体内容中获取news_name、news_time、news_link，并存到列表中
        result02 = reCode_02.finditer(i.group())
        for index, j in enumerate(result02):
            newsNameList.append(j.group("news_name"))
            newsTimeList.append(j.group("news_time"))
            newUrl = "http://teach.dlut.edu.cn/" + j.group("news_link").strip("../")
            newsLinkList.append(newUrl)
            # 使用SQl命令将列表中的值插入到表中
            sql = "INSERT INTO dlut_spider(id,news_name,news_time,news_link) VALUES (%s,%s,%s,%s)"
            cursor01.execute(sql, (m * 20 + index, j.group("news_name"), j.group("news_time"), newUrl))
            database01.commit()
    cursor01.close()  # 关闭游标
    database01.close()  # 关闭连接
