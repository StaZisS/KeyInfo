import('../../../../styles/peopleItem.css')

export const ButtonTeacher = ({roles,callback}) => {
    return(
        <button onClick={callback} className={`${roles.includes('TEACHER') ? 'button-role delit' : 'button-role'} py-2 px-4`}>
            <span className='buttonName'>Преподаватель</span>
        </button>
    )
}
