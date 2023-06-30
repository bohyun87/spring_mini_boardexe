create table boardexe (
    num number primary key,
    subject varchar2(255),
    name varchar2(100),
    phone varchar2(100),
    sns varchar2(100),    
    hikingDate varchar2(100),
    course varchar2(255),
    content clob,
    pwd varchar2(100),
    regDate date,
    hitCount number
);

select * from boardexe;

commit;
ROLLBACK;


select nvl(max(num), 0) from boardexe;

alter table boardexe modify hikingDate varchar2(50);

rollback;
select * from (
                select rownum rnum, data.* 
                from ( select num, subject, name, phone, sns, hikingDate, course, content, to_char(regDate, 'yyyy-mm-dd') regDate   
                         from boardexe
                         where subject like '%°æÁÖ%'
                         order by num desc 
                      )data
                );


select * from (
                select rownum rnum, data.* 
                from ( select num, subject, name, phone, sns, hikingDate, course, content, to_char(regDate, 'yyyy-mm-dd') regDate   
                         from boardexe
                         where ${searchKey} like '%' || #{searchValue} || '%'
                         order by num desc 
                      )data
                      
                )
                    where rnum >= #{start} and rnum <= #{end};
                
select nvl(count(*), 0) from boardexe where subject like '%¼³¾Ç%';


update boardexe set hitCount = hitCount + 1 where num = 1;



select * from boardexe where num = 1;
 rollback;
 
update boardexe set subject=#{subject}, name=#{name}, phone=#{phone}, sns=#{sns}, hikingDate=#{hikingDate}, course=#{course}, content=#{content}, pwd=#{pwd} where num=#{num}

