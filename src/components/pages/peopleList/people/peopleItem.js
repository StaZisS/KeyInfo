import {ButtonStudent} from './studentRoleButton'
import {ButtonTeacher} from './teacherRoleButton'
import {useMutation, useQueryClient} from "react-query";
import UserService from "../../../../services/UserService";

import('../../../../styles/peopleItem.css')

export const PeopleItem = ({userId, name, email}) => {

    const queryClient = useQueryClient()
    const mutation = useMutation((params) => UserService.addRole(params.userId, params.clientRole),{
        onSuccess(){
            queryClient.invalidateQueries(['users'])
        }
    })

    const handleAddRole = (clientRole) => {
        mutation.mutate({userId, clientRole})
    }

    return (
        <div className='w-100 d-flex item border flex-nowrap justify-content-between'>

            <div className='d-flex h-100 align-items-center'>
                <div className='ps-3 d-flex align-items-center flex-grow-0 gap-3 number'>
                    <span>ФИО: {name}</span>
                    <span>email: {email}</span>
                </div>
            </div>
            <div className="buttons d-flex h-100 align-items-center">
                <ButtonStudent callback={() => handleAddRole(['STUDENT'])}/>
                <ButtonTeacher callback={() => handleAddRole(['TEACHER'])}/>
            </div>

        </div>
    )
}