package validation

import staticWAD.WADStatic


class ValidationProject {
    companion object {
        fun nameValidation(text : String) : Pair<String, Int>
        {
            val regex = "^[A-Za-z0-9]*$".toRegex()
            return if (text == ""){
                Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_project_name_field_should_not_be_empty"]}\n",2)
            } else if (!regex.matches(text)) {
                Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_project_name_field_should_consist_only_of_A-Za-z0-9"]}\n",2)
            } else{
                Pair("",0)
            }
        }
        fun domenNameValidation(text : String) : Pair<String, Int>
        {
            val regex = "^(?!https?).*$".toRegex()
            return if (text == ""){
                Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_domen_name_field_should_not_be_empty"]}\n",2)
            } else if (!regex.matches(text)) {
                Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_domen_name_field_must_consist_of_a_url_without_a_protocol,_www_is_not_required"]}\n",2)
            } else{
                Pair("",0)
            }
        }
        fun dateTimeValidation(text: String, min: String, max: String, fildName: String?) : Pair<String, Int>
        {
            val regex = "^[0-9].*$".toRegex()
            if (text == ""){
                return Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_field_should_not_be_empty__part1"]}$fildName${WADStatic.WADconst.labels["ValidationProject__error__The_field_should_not_be_empty__part2"]}\n",2)
            } else if (!regex.matches(text)) {
                return Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_field_should_consist_only_of_0-9"]}\n",2)
            } else{
                if (text.length > 14){
                    return Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_field_can_contain_a_maximum_of_14_characters"]}\n",1)
                } else{
                    if (min != ""){
                        if (text.padEnd(14,'0') < min){
                            return Pair("${WADStatic.WADconst.labels["ValidationProject__error__Date_and_time_less_than_minimum"]}${min}\n",1)
                        } else{
                            return Pair("",0)
                        }
                    }
                    if (max != ""){
                        if (text.padEnd(14,'0') > max){
                            return Pair("${WADStatic.WADconst.labels["ValidationProject__error__Date_and_time_greater_than_the_maximum"]}${max}\n",1)
                        } else{
                            return Pair("",0)
                        }
                    }
                }
                return Pair("",0)
            }
        }
        fun directotyValidation(text : String) : Pair<String,Int>
        {
            val regex = "^[A-Za-z0-9:]*\$".toRegex()
            if (text == ""){
                return Pair("${WADStatic.WADconst.labels["ValidationProject__error__The_directory_field_should_not_be_empty"]}\n",2)
            } else if (!regex.matches(text.replace("\\",""))) {
                return Pair("${WADStatic.WADconst.labels["ValidationProject__error__Invalid_directory_path"]}\n",2)
            } else {
                return Pair("",0)
            }
        }
    }
}