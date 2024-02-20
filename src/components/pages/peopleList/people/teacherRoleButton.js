import('../../../../styles/peopleItem.css')

export const ButtonTeacher = ({roles,callback}) => {
    return(
        <button onClick={callback} className={roles.includes('TEACHER') ? 'button-role delit' : 'button-role'}>
            <span className='buttonName'>Преподаватель</span>
        </button>
    )
}
