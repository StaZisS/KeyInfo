import {ButtonStudent} from './studentRoleButton'
import {ButtonTeacher} from './teacherRoleButton'
import {useMutation, useQueryClient} from "react-query";
import UserService from "../../../../services/UserService";

import('../../../../styles/peopleItem.css')

export const PeopleItem = ({userId, name, email, roles}) => {
    const queryClient = useQueryClient()
    const mutation = useMutation((params) => UserService.addRole(params.userId, params.clientRole), {
        onSuccess() {
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
                    <span>ФИО: <span className={'fw-bold'}>{name}</span></span>
                    <span>Email: <span className={'fw-bold'}>{email}</span></span>
                </div>
            </div>
            <div className="buttons d-flex h-100 align-items-center">
                {/*{roles.includes('TEACHER') &&*/}
                {/*    <ButtonTeacher roles={roles} callback={() => handleAddRole(['TEACHER'])}/>*/}
                {/*}*/}
                {/*{roles.includes('STUDENT') &&*/}
                {/*    <ButtonStudent roles={roles} callback={() => handleAddRole(['STUDENT'])}/>*/}
                {/*}*/}
                {roles.includes('UNSPECIFIED') &&
                    <>
                        <ButtonStudent roles={roles} callback={() => handleAddRole(['STUDENT'])}/>
                        <ButtonTeacher roles={roles} callback={() => handleAddRole(['TEACHER'])}/>
                    </>
                }


            </div>

        </div>
    )
}