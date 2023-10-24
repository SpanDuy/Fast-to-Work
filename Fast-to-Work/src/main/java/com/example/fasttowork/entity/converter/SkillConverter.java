package com.example.fasttowork.entity.converter;

import com.example.fasttowork.entity.Skill;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class SkillConverter implements Converter<String, Skill> {

    @Override
    public Skill convert(String source) {
        Skill skill = new Skill();
        skill.setSkill(source);
        return skill;
    }
}
