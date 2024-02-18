import('../../../../styles/peopleItem.css')

export const ButtonTeacher = ({roles,callback}) => {
    return(
        <button onClick={callback} className={roles.includes('TEACHER') ? 'button-role bg-danger' : 'button-role'}>
            <span className='buttonName'>Преподаватель</span>
        </button>
    )
}
